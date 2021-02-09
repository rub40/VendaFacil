package com.example.vendafacil.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vendafacil.DAO.SaleDAO
import com.example.vendafacil.NewSaleActivity
import com.example.vendafacil.R
import com.example.vendafacil.adapters.SaleRecyclerViewAdapter
import com.example.vendafacil.helpers.Callback
import com.example.vendafacil.helpers.CallbackAsync
import com.example.vendafacil.helpers.awaitCallback
import com.example.vendafacil.models.Sale
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.Dispatcher
import kotlinx.android.synthetic.main.list_sale_fragment.*
import kotlinx.coroutines.*


class ListSaleFragment : Fragment() {

    private lateinit var saleAdapter: SaleRecyclerViewAdapter
    private lateinit var deleteIcon: Drawable;
    private lateinit var archiveIcon: Drawable;
    private lateinit var swipeBackground: ColorDrawable;
    private var mProgressBar: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        return inflater.inflate(R.layout.list_sale_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view?.findViewById<FloatingActionButton>(R.id.btnNovoPedido)?.setOnClickListener {
            val intent = Intent(activity, NewSaleActivity::class.java)
            startActivity(intent)
        }

        mProgressBar = ProgressDialog(activity)
        initRecyclerView()
        initRecyclerViewSwipe()
        addDataSet()
    }

    override fun onResume() {
        super.onResume()
        addDataSet()
    }


    private fun initRecyclerViewSwipe() {
        val itemTouchHelperCallBack = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false;
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.RIGHT ->
                        (saleAdapter as SaleRecyclerViewAdapter).removeItem(viewHolder as SaleRecyclerViewAdapter.SaleViewHolder)
                    ItemTouchHelper.LEFT ->
                        (saleAdapter as SaleRecyclerViewAdapter).archiveItem(viewHolder as SaleRecyclerViewAdapter.SaleViewHolder)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                val itemView = viewHolder.itemView
                val iconMarginDelete = (itemView.height - deleteIcon.intrinsicHeight) / 2
                val iconMarginArchive = (itemView.height - archiveIcon.intrinsicHeight) / 2

                if (dX > 0) {
                    swipeBackground = ColorDrawable(android.graphics.Color.parseColor("#FF6666"))
                    swipeBackground.setBounds(
                        itemView.left,
                        itemView.top,
                        dX.toInt(),
                        itemView.bottom
                    )
                    deleteIcon.setBounds(
                        itemView.left + iconMarginDelete,
                        itemView.top + iconMarginDelete,
                        itemView.left + iconMarginDelete + deleteIcon.intrinsicWidth,
                        itemView.bottom - iconMarginDelete
                    )
                } else {
                    swipeBackground = ColorDrawable(android.graphics.Color.parseColor("#66FF66"))
                    swipeBackground.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    archiveIcon.setBounds(
                        itemView.right - iconMarginArchive - deleteIcon.intrinsicWidth,
                        itemView.top + iconMarginArchive,
                        itemView.right - iconMarginArchive,
                        itemView.bottom - iconMarginArchive
                    )
                }

                swipeBackground.draw(c)
                c.save()

                if (dX > 0) {
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                    deleteIcon.draw(c)

                } else {
                    c.clipRect(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    archiveIcon.draw(c)
                }

                c.restore()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView_pedido_list)
    }

    private fun initRecyclerView() {
        deleteIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_delete)!!
        archiveIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_archive)!!

        recyclerView_pedido_list.apply() {
            layoutManager = LinearLayoutManager(context)
            saleAdapter = SaleRecyclerViewAdapter()
            adapter = saleAdapter
        }
    }

    private fun addDataSet() = runBlocking {
        saleAdapter.submitList(mutableListOf<Sale>())
        GlobalScope.launch(Dispatchers.Main) {
            mProgressBar?.setMessage("Carregando")
            mProgressBar?.show()

            var lista = awaitCallback<MutableList<Sale>> { SaleDAO.buscarSale(it, false) }
            saleAdapter.submitList(lista)
            saleAdapter.notifyDataSetChanged()

            mProgressBar?.hide()
        }
    }
}
