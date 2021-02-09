package com.example.vendafacil

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vendafacil.DAO.SaleDAO
import com.example.vendafacil.adapters.SaleRecyclerViewAdapter
import com.example.vendafacil.helpers.awaitCallback
import com.example.vendafacil.models.Sale
import kotlinx.android.synthetic.main.activity_help.toolbar_help
import kotlinx.android.synthetic.main.activity_relatorio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RelatorioActivity : AppCompatActivity() {

    private lateinit var saleAdapter: SaleRecyclerViewAdapter
    private var mProgressBar: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_relatorio)

        setSupportActionBar(toolbar_help);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "Voltar"

        mProgressBar = ProgressDialog(this)
        initRecyclerView()
        addDataSet()
    }

    override fun onResume() {
        super.onResume()
        addDataSet()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {

        recyclerView_pedido_list_faturado.apply() {
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

            var lista = awaitCallback<MutableList<Sale>> { SaleDAO.buscarSale(it, true) }
            AtribuirTotais(lista);

            saleAdapter.submitList(lista)
            saleAdapter.notifyDataSetChanged()
            mProgressBar?.hide()
        }
    }

    private fun AtribuirTotais(list: MutableList<Sale>) {

        val valor =listOf<Double>(list.sumByDouble { x -> x.valueSale }).toString().replace("[", "").replace("]", "").toDouble();
        var valorCorrigido:String = "%.2f".format(valor)
        et_total.setText(valorCorrigido)
    }
}
