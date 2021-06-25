package br.com.alura.technews.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.fragment.extensions.mostraErro
import br.com.alura.technews.ui.recyclerview.adapter.ListaNoticiasAdapter
import br.com.alura.technews.ui.viewmodel.ListaNoticiasViewModel
import kotlinx.android.synthetic.main.lista_noticias.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val MENSAGEM_FALHA_CARREGAR_NOTICIAS = "Não foi possível carregar as novas notícias"

private const val TITULO_APPBAR = "Notícias"

class ListaNoticiaFragment:Fragment() {
    private val viewModel by viewModel<ListaNoticiasViewModel>()

    private val adapter by lazy {
        context?.let{
            ListaNoticiasAdapter(context = it)
        }?: throw IllegalArgumentException("Contexto inválido")
    }

    var onFabSaveNewClicked: () -> Unit = {}
    var onSelectNew: (noticia:Noticia) -> Unit = {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buscaNoticias()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.lista_noticias, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraRecyclerView()
        configuraFabAdicionaNoticia()
        activity?.title = TITULO_APPBAR
    }

    private fun configuraFabAdicionaNoticia() {
        lista_noticias_fab_salva_noticia.setOnClickListener {
            onFabSaveNewClicked()
        }
    }

    private fun configuraRecyclerView() {
        val divisor = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        lista_noticias_recyclerview.addItemDecoration(divisor)
        lista_noticias_recyclerview.adapter = adapter
        configuraAdapter()
    }

    private fun configuraAdapter() {
        adapter.quandoItemClicado = onSelectNew
    }

    private fun buscaNoticias() {
        viewModel.buscaTodos().observe(this, Observer {resource ->

            resource.dado?.let{
                adapter.atualiza(it)
            }

            resource.erro?.let{
                mostraErro(MENSAGEM_FALHA_CARREGAR_NOTICIAS)
            }

        })
    }



}