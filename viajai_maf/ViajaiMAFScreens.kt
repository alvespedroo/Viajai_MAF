package com.example.viajai_maf

import androidx.compose.foundation.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

private val vermelho = Color(0xFF8B261D)
private val laranja = Color(0xFFEA813C)
private val creme = Color(0xFFFFF7EF)
private val fundo = Brush.verticalGradient(listOf(laranja, vermelho))
private fun dinheiro(valor: Double): String = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valor)

data class ViagemMaf(val id: Int, val origem: String, val destino: String, val data: String, val orcamento: Double)
data class BagagemMaf(val id: Int, val nome: String, val quantidade: Int, val preparado: Boolean)
data class DespesaMaf(val id: Int, val descricao: String, val categoria: String, val valor: Double, val viagemId: Int = 1)
data class PreparoMaf(val id: Int, val categoria: String, val nome: String, val marcado: Boolean, val viagemId: Int = 1)

class ViajaiDados {
    private var contador = 20
    fun proximoId(): Int = ++contador
    val lugaresSalvos = mutableStateListOf<Int>()
    val viagensConcluidas = mutableStateListOf<ViagemMaf>()
    val viagens = mutableStateListOf<ViagemMaf>()
    var viagemSelecionadaId by mutableIntStateOf(-1)
    val viagemAtual: ViagemMaf? get() = viagens.firstOrNull { it.id == viagemSelecionadaId } ?: viagens.firstOrNull()
    fun scoreDaViagem(id: Int): Int { val lista = preparo.filter { it.viagemId == id }; return if (lista.isEmpty()) 0 else lista.count { it.marcado } * 100 / lista.size }
    fun gastosDaViagem(id: Int): Double = despesas.filter { it.viagemId == id }.sumOf { it.valor }
    val bagagens = mutableStateListOf(BagagemMaf(1, "Roupas", 5, true), BagagemMaf(2, "Carregador", 1, false))
    val despesas = mutableStateListOf<DespesaMaf>()
    val preparo = mutableStateListOf<PreparoMaf>()
    fun criarChecklistPadrao(viagemId: Int) {
        listOf("Documentos" to "RG", "Documentos" to "CNH", "Revisão do carro" to "Pneus", "Revisão do carro" to "Estepe", "Emergência" to "Kit médico").forEach { (categoria, nome) ->
            preparo.add(PreparoMaf(proximoId(), categoria, nome, false, viagemId))
        }
    }
    val score: Int get() {
        return viagemAtual?.let { scoreDaViagem(it.id) } ?: 0
    }
}

@Composable private fun Fundo(titulo: String, onVoltar: (() -> Unit)? = null, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().background(fundo).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onVoltar != null) IconButton(onClick = onVoltar) { Icon(Icons.Default.ArrowBack, "Voltar", tint = Color.White) }
            Text(titulo, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))
        Column(Modifier.weight(1f).fillMaxWidth(), content = content)
    }
}
@Composable private fun Cartao(content: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) { Column(Modifier.padding(14.dp), content = content) }
}
@Composable private fun Barra(score: Int) {
    Text("Preparação: $score%", fontWeight = FontWeight.Bold, color = vermelho)
    LinearProgressIndicator(progress = { score / 100f }, modifier = Modifier.fillMaxWidth().height(8.dp),
        color = laranja, trackColor = creme)
}
@Composable fun ChecklistPainelScreen(dados: ViajaiDados, onViagem: () -> Unit, onBagagem: () -> Unit, onGastos: () -> Unit, onDetalhe: (Int) -> Unit) {
    val viagem = dados.viagemAtual
    var novoBloco by remember { mutableStateOf("") }
    var novoItem by remember { mutableStateOf("") }
    var blocoAtual by remember { mutableStateOf("Documentos") }
    var editarBloco by remember { mutableStateOf<String?>(null) }
    var nomeEditado by remember { mutableStateOf("") }
    var editarItemId by remember { mutableStateOf<Int?>(null) }
    var itemEditado by remember { mutableStateOf("") }
    Fundo("Checklist") {
        if (viagem == null) {
            Cartao { Text("Nenhuma viagem em planejamento."); Button(onClick = onViagem) { Text("+ Planejar viagem") } }
        } else {
            val itens = dados.preparo.filter { it.viagemId == viagem.id }
            val blocos = itens.map { it.categoria }.distinct()
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item { Cartao {
                    Text(viagem.destino, style = MaterialTheme.typography.titleLarge, color = vermelho, fontWeight = FontWeight.Bold)
                    Barra(dados.scoreDaViagem(viagem.id))
                    Text("${itens.count { it.marcado }}/${itens.size} itens concluídos")
                    Text("Orçamento: ${dinheiro(viagem.orcamento)}")
                    TextButton(onClick = { onDetalhe(viagem.id) }) { Text("Detalhes e concluir viagem") }
                } }
                item { Cartao {
                    Text("Selecionar viagem", fontWeight = FontWeight.Bold)
                    dados.viagens.forEach { v -> TextButton(onClick = { dados.viagemSelecionadaId = v.id }) { Text(v.destino + if (v.id == viagem.id) " ✓" else "") } }
                } }
                items(blocos, key = { "bloco_${viagem.id}_$it" }) { bloco ->
                    Cartao {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(bloco, fontWeight = FontWeight.Bold, color = vermelho, modifier = Modifier.weight(1f))
                            TextButton(onClick = { editarBloco = bloco; nomeEditado = bloco }) { Text("Editar") }
                            IconButton(onClick = { dados.preparo.removeAll { it.viagemId == viagem.id && it.categoria == bloco } }) { Icon(Icons.Default.Delete, "Excluir bloco") }
                        }
                        if (editarBloco == bloco) {
                            OutlinedTextField(nomeEditado, { nomeEditado = it }, label = { Text("Nome do bloco") }, modifier = Modifier.fillMaxWidth())
                            Button(onClick = {
                                if (nomeEditado.isNotBlank()) dados.preparo.indices.filter { dados.preparo[it].viagemId == viagem.id && dados.preparo[it].categoria == bloco }.forEach { i -> dados.preparo[i] = dados.preparo[i].copy(categoria = nomeEditado.trim()) }
                                editarBloco = null
                            }) { Text("Salvar nome") }
                        }
                        itens.filter { it.categoria == bloco }.forEach { tarefa ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(tarefa.marcado, onCheckedChange = { marcado ->
                                    val i = dados.preparo.indexOfFirst { it.id == tarefa.id }
                                    if (i >= 0) dados.preparo[i] = tarefa.copy(marcado = marcado)
                                })
                                if (editarItemId == tarefa.id) {
                                    OutlinedTextField(itemEditado, { itemEditado = it }, modifier = Modifier.weight(1f), singleLine = true)
                                    TextButton(onClick = {
                                        val i = dados.preparo.indexOfFirst { it.id == tarefa.id }
                                        if (i >= 0 && itemEditado.isNotBlank()) dados.preparo[i] = tarefa.copy(nome = itemEditado.trim())
                                        editarItemId = null
                                    }) { Text("OK") }
                                } else {
                                    Text(tarefa.nome, modifier = Modifier.weight(1f).clickable { editarItemId = tarefa.id; itemEditado = tarefa.nome })
                                }
                                IconButton(onClick = { dados.preparo.removeAll { it.id == tarefa.id } }) { Icon(Icons.Default.Delete, "Excluir item") }
                            }
                        }
                        OutlinedTextField(novoItem, { novoItem = it }, label = { Text("Novo item em $bloco") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                        TextButton(onClick = { if (novoItem.isNotBlank()) { dados.preparo.add(PreparoMaf(dados.proximoId(), bloco, novoItem.trim(), false, viagem.id)); novoItem = "" } }) { Text("+ Adicionar item") }
                    }
                }
                item { Cartao {
                    Text("Adicionar bloco", fontWeight = FontWeight.Bold)
                    OutlinedTextField(novoBloco, { novoBloco = it }, label = { Text("Nome do bloco") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Button(onClick = { if (novoBloco.isNotBlank() && novoBloco !in blocos) { dados.preparo.add(PreparoMaf(dados.proximoId(), novoBloco.trim(), "Novo item", false, viagem.id)); novoBloco = "" } }) { Text("+ Criar bloco") }
                } }
                item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onBagagem, modifier = Modifier.weight(1f)) { Text("Bagagem") }
                    Button(onClick = onGastos, modifier = Modifier.weight(1f)) { Text("Gastos") }
                } }
            }
        }
    }
}
@Composable fun BagagemViajaiScreen(dados: ViajaiDados, onVoltar: () -> Unit, onDetalhe: (Int) -> Unit) {
    var nome by remember { mutableStateOf("") }; var qtd by remember { mutableStateOf("1") }
    Fundo("Minha Bagagem", onVoltar) {
        LazyColumn {
            item { Cartao {
                Text("Adicionar item", fontWeight = FontWeight.Bold)
                OutlinedTextField(nome, { nome = it }, label = { Text("Item") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(qtd, { qtd = it.filter(Char::isDigit) }, label = { Text("Quantidade") }, modifier = Modifier.fillMaxWidth())
                Button(onClick = { val n = qtd.toIntOrNull(); if (nome.isNotBlank() && n != null && n > 0) { dados.bagagens.add(BagagemMaf(dados.proximoId(), nome.trim(), n, false)); nome = ""; qtd = "1" } }) { Text("Adicionar") }
            } }
            items(dados.bagagens, key = { it.id }) { item -> Cartao {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(item.preparado, onCheckedChange = { v -> val i = dados.bagagens.indexOfFirst { it.id == item.id }; if (i >= 0) dados.bagagens[i] = item.copy(preparado = v) })
                    Column(Modifier.weight(1f).clickable { onDetalhe(item.id) }) { Text(item.nome, fontWeight = FontWeight.Bold); Text("Quantidade: ${item.quantidade} • Detalhes →") }
                    IconButton(onClick = { dados.bagagens.remove(item) }) { Icon(Icons.Default.Delete, "Remover") }
                }
            } }
        }
    }
}
@Composable fun DetalheBagagemScreen(dados: ViajaiDados, id: Int, onVoltar: () -> Unit) {
    val item = dados.bagagens.firstOrNull { it.id == id }
    Fundo("Detalhes da bagagem", onVoltar) { Cartao {
        if (item == null) Text("Item não encontrado") else {
            Text(item.nome, style = MaterialTheme.typography.titleLarge)
            Text("Quantidade: ${item.quantidade}")
            Text(if (item.preparado) "Já está na mala" else "Ainda falta colocar na mala")
            Button(onClick = { val i = dados.bagagens.indexOfFirst { it.id == id }; if (i >= 0) dados.bagagens[i] = item.copy(preparado = !item.preparado) }) { Text("Alternar preparação") }
        }
    } }
}
@Composable fun DetalheViagemScreen(dados: ViajaiDados, id: Int, onVoltar: () -> Unit, onGastos: () -> Unit) {
    val viagem = dados.viagens.firstOrNull { it.id == id }
    Fundo("Detalhes da viagem", onVoltar) { Cartao {
        if (viagem == null) Text("Viagem não encontrada") else {
            Text(viagem.destino, style = MaterialTheme.typography.titleLarge)
            Text("Origem: ${viagem.origem} • Data: ${viagem.data}")
            Spacer(Modifier.height(12.dp)); Barra(dados.scoreDaViagem(viagem.id))
            Text("Orçamento: ${dinheiro(viagem.orcamento)}")
            val total = dados.gastosDaViagem(viagem.id)
            Text("Gastos registrados: ${dinheiro(total)}")
            Text("Saldo estimado: ${dinheiro(viagem.orcamento - total)}")
            Button(onClick = onGastos) { Text("Gerenciar gastos") }
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                dados.viagensConcluidas.add(viagem)
                dados.viagens.removeAll { it.id == viagem.id }
                dados.viagemSelecionadaId = dados.viagens.firstOrNull()?.id ?: -1
                onVoltar()
            }, colors = ButtonDefaults.buttonColors(containerColor = vermelho)) { Text("Concluir viagem") }
        }
    } }
}
@Composable fun GastosViajaiScreen(dados: ViajaiDados, onVoltar: () -> Unit) {
    var nome by remember { mutableStateOf("") }; var valor by remember { mutableStateOf("") }
    val viagem = dados.viagemAtual
    val total = viagem?.let { dados.gastosDaViagem(it.id) } ?: 0.0
    val orcamento = viagem?.orcamento ?: 0.0
    Fundo("Total de Gastos", onVoltar) {
        LazyColumn {
            item { Cartao {
                Text("Total gasto", color = vermelho)
                Text(dinheiro(total), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Orçamento: ${dinheiro(orcamento)}")
                LinearProgressIndicator(progress = { if (orcamento <= 0) 0f else (total / orcamento).toFloat().coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth(), color = laranja)
                Text("Saldo: ${dinheiro(orcamento - total)}")
            } }
            item { Cartao {
                Text("Nova despesa", fontWeight = FontWeight.Bold)
                OutlinedTextField(nome, { nome = it }, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(valor, { valor = it }, label = { Text("Valor (R$)") }, modifier = Modifier.fillMaxWidth())
                Button(onClick = { val v = valor.replace(',', '.').toDoubleOrNull(); if (viagem != null && nome.isNotBlank() && v != null && v > 0) { dados.despesas.add(DespesaMaf(dados.proximoId(), nome.trim(), "Outros", v, viagem.id)); nome = ""; valor = "" } }) { Text("Adicionar gasto") }
            } }
            items(dados.despesas.filter { it.viagemId == viagem?.id }, key = { "despesa_${it.id}" }) { despesa -> Cartao {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) { Text(despesa.descricao, fontWeight = FontWeight.Bold); Text(dinheiro(despesa.valor)) }
                    IconButton(onClick = { dados.despesas.remove(despesa) }) { Icon(Icons.Default.Delete, "Remover despesa") }
                }
            } }
        }
    }
}
@Composable
fun PerfilViajaiScreen(dados: ViajaiDados, onViagens: () -> Unit, onAbrirLugar: (Int) -> Unit) {
    var filtroSalvos by remember { mutableStateOf("Todos") }
    var nome by remember { mutableStateOf("Nome de usuário") }
    Fundo("Meu Perfil") {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Cartao {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Surface(shape = RoundedCornerShape(50), color = Color(0xFFFFE5D9), modifier = Modifier.size(58.dp)) {
                        Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, "Avatar", tint = vermelho, modifier = Modifier.size(35.dp)) }
                    }
                    Column {
                        Text(nome, fontWeight = FontWeight.Bold, color = vermelho, style = MaterialTheme.typography.titleMedium)
                        Text("Meu perfil de viajante", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(nome, { nome = it }, label = { Text("Editar nome") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            }
            Cartao {
                Text("Lugares Salvos", fontWeight = FontWeight.Bold, color = vermelho, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Todos", "Cidades", "Hotéis", "Restaurantes", "Passeios").forEach { opcao ->
                        FilterChip(selected = filtroSalvos == opcao, onClick = { filtroSalvos = opcao },
                            label = { Text(opcao, style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFFFE4D5)))
                    }
                }
                val salvosFiltrados = lugaresExplorar.filter { lugar ->
                    lugar.id in dados.lugaresSalvos && (filtroSalvos == "Todos" || lugar.categoria == filtroSalvos)
                }
                if (salvosFiltrados.isEmpty()) {
                    Text(if (dados.lugaresSalvos.isEmpty()) "Você ainda não salvou nenhum lugar." else "Nenhum lugar salvo nesta categoria.",
                        style = MaterialTheme.typography.bodyMedium)
                }
                salvosFiltrados.forEach { lugar ->
                    Card(onClick = { onAbrirLugar(lugar.id) }, modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                        shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = creme)) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                            Image(painter = painterResource(lugar.foto), contentDescription = lugar.nome,
                                contentScale = ContentScale.Crop, modifier = Modifier.size(width = 94.dp, height = 76.dp))
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Text(lugar.nome, fontWeight = FontWeight.Bold, color = vermelho, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                Text(lugar.regiao, style = MaterialTheme.typography.bodySmall)
                                Text("Ver detalhes ›", color = vermelho, style = MaterialTheme.typography.labelSmall)
                            }
                            IconButton(onClick = { dados.lugaresSalvos.remove(lugar.id) }) {
                                Icon(Icons.Default.Delete, "Remover dos salvos", tint = vermelho)
                            }
                        }
                    }
                }
            }
            Cartao {
                Text("Histórico de viagens", fontWeight = FontWeight.Bold, color = vermelho)
                if (dados.viagensConcluidas.isEmpty()) Text("Nenhuma viagem concluída.")
                dados.viagensConcluidas.forEach { Text("${it.destino} • ${it.data}") }
            }
            Cartao {
                Text("Minhas viagens", fontWeight = FontWeight.Bold, color = vermelho)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(Modifier.weight(1f)) { Text("${dados.viagens.size}", style = MaterialTheme.typography.headlineSmall, color = vermelho); Text("Viagens planejadas", style = MaterialTheme.typography.bodySmall) }
                    Column(Modifier.weight(1f)) { Text("${dados.score}%", style = MaterialTheme.typography.headlineSmall, color = vermelho); Text("Preparação", style = MaterialTheme.typography.bodySmall) }
                }
                Spacer(Modifier.height(8.dp))
                dados.viagens.forEach { viagem ->
                    HorizontalDivider()
                    Spacer(Modifier.height(8.dp))
                    Text(viagem.destino, fontWeight = FontWeight.SemiBold)
                    Text("${viagem.data}  •  ${viagem.origem}", style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(8.dp))
                }
                Button(onClick = onViagens, colors = ButtonDefaults.buttonColors(containerColor = vermelho), modifier = Modifier.fillMaxWidth()) { Text("Ver minhas viagens") }
            }
        }
    }
}
