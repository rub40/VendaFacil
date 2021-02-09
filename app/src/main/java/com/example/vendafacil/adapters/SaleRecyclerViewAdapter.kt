package com.example.vendafacil.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vendafacil.DAO.SaleDAO
import com.example.vendafacil.R
import com.example.vendafacil.models.Sale
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.list_sale_item_fragment.view.*

class SaleRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private lateinit var items:  MutableList<Sale>;


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SaleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_sale_item_fragment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is SaleViewHolder ->{
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size;
    }

    fun removeItem(viewHolder: SaleViewHolder){
        var removedPosition = viewHolder.adapterPosition
        var removedItem: Sale = items[viewHolder.adapterPosition]

        items.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)
        SaleDAO.deleteSale(removedItem.idSale)

//        Snackbar.make(viewHolder.itemView, "${removedItem.idSale} deletado.",Snackbar.LENGTH_LONG).setAction("DESFAZER"){
//            items.add(removedPosition, removedItem)
//            notifyItemInserted(removedPosition)
//            return@setAction
//        }.show()


    }

    fun archiveItem(viewHolder: SaleViewHolder){
        var archivedPosition = viewHolder.adapterPosition
        var archivedItem = items[viewHolder.adapterPosition]

        items.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)
        SaleDAO.updateSaleFaturado(archivedItem.idSale, true)
//
//        Snackbar.make(viewHolder.itemView, "${archivedItem.idSale} arquvido.", Snackbar.LENGTH_LONG).setAction("DESFAZER"){
//            items.add(archivedPosition, archivedItem)
//            notifyItemInserted(archivedPosition)
//            return@setAction
//        }.show()

    }

    fun submitList(saleList: MutableList<Sale>){
        items = saleList;
    }

    class SaleViewHolder constructor( itemView: View): RecyclerView.ViewHolder(itemView) {

        val saleId = itemView.sale_id
        val saleName = itemView.sale_name
        val saleValue = itemView.sale_value
        val saleData = itemView.sale_data

        fun bind(sale: Sale) {

            saleId.setText(sale.idSale)
            saleName.setText(sale.name)
            saleValue.setText(sale.valueSale.toString())
            saleData.setText(sale.dataSale)

        }
    }
}