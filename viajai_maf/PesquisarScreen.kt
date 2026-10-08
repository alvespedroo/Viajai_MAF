package com.example.viajai_maf

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val corPrincipal = Color(0xFF922D24)
private val corLaranja = Color(0xFFEB8448)

data class LugarExplorar(val id: Int, val nome: String, val regiao: String, val categoria: String,
                         val nota: String, val descricao: String, val foto: Int)

val lugaresExplorar = listOf(
    LugarExplorar(1, "Pousada Maraka", "São Paulo, SP", "Hotéis", "4,73", "Hospedagem aconchegante para sua viagem.", R.drawable.pousada_maraka),
    LugarExplorar(2, "Casa São Paulo", "São Paulo, SP", "Hotéis", "4,98", "148 avaliações • Total R$ 1.505", R.drawable.pousada_sao_paulo),
    LugarExplorar(3, "Pousada Zamberlan", "São Paulo, SP", "Hotéis", "4,86", "Pousada tradicional para descansar.", R.drawable.pousada_zamberlan),
    LugarExplorar(4, "Casa do Porco", "São Paulo, SP", "Restaurantes", "4,8", "Culinária brasileira em São Paulo.", R.drawable.casa_do_porco_foto),
    LugarExplorar(5, "Restaurante Manu", "Curitiba, PR", "Restaurantes", "4,9", "Gastronomia em Curitiba.", R.drawable.manu_foto),
    LugarExplorar(6, "Jardim Botânico", "Curitiba, PR", "Passeios", "4,8", "Jardins, arquitetura e natureza.", R.drawable.jardim_botanico_foto),
    LugarExplorar(7, "Praias de Florianópolis", "Florianópolis, SC", "Passeios", "4,7", "Mar e natureza em Santa Catarina.", R.drawable.praia_florianopolis_foto),
    LugarExplorar(8, "Gramado", "Gramado, RS", "Cidades", "4,7", "Conheça a Serra Gaúcha.", R.drawable.gramado),
    LugarExplorar(9, "Balneário Camboriú", "Balneário Camboriú, SC", "Cidades", "—", "Praias e atrações do litoral catarinense.", R.drawable.balneario_camboriu),
    LugarExplorar(10, "Cataratas do Iguaçu", "Foz do Iguaçu, PR", "Passeios", "—", "Paisagens e trilhas nas cataratas.", R.drawable.cataratas_iguacu),
    LugarExplorar(11, "Cristo Redentor", "Rio de Janeiro, RJ", "Passeios", "—", "Um dos principais pontos turísticos do Rio.", R.drawable.cristo_redentor),
    LugarExplorar(12, "Pelourinho", "Salvador, BA", "Passeios", "—", "Centro histórico, cultura e arquitetura.", R.drawable.pelourinho_bahia),
    LugarExplorar(13, "Recife", "Recife, PE", "Cidades", "—", "Explore atrações e paisagens da cidade.", R.drawable.recife_foto),
    LugarExplorar(14, "Rio de Janeiro", "Rio de Janeiro, RJ", "Cidades", "—", "Praias e cartões-postais da cidade.", R.drawable.rio_de_janeiro),
    LugarExplorar(15, "Curitiba", "Curitiba, PR", "Cidades", "—", "Parques e atrações da capital paranaense.", R.drawable.curitiba_parana)
)

fun lugarNome(id: Int): String = lugaresExplorar.firstOrNull { it.id == id }?.nome ?: "Lugar #$id"

@Composable
fun PesquisarScreen(onSelecionarDestino: (Int) -> Unit, dados: ViajaiDados) {
    var busca by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Cidades") }
    val filtrados = lugaresExplorar.filter { lugar ->
        lugar.categoria == categoria && (busca.isBlank() || listOf(lugar.nome, lugar.regiao, lugar.descricao)
            .any { it.contains(busca.trim(), ignoreCase = true) })
    }
    Column(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(corLaranja, corPrincipal))).padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(16.dp))
        Text("Explorar destinos", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(busca, { busca = it }, modifier = Modifier.fillMaxWidth(), singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.Search, "Pesquisar") },
            placeholder = { Text("Buscar em ${categoria.lowercase()}...") }, shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White))
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(3.dp), verticalAlignment = Alignment.CenterVertically) {
            listOf("Cidades", "Hotéis", "Restaurantes", "Passeios").forEach { opcao ->
                FilterChip(selected = categoria == opcao, onClick = { categoria = opcao },
                    label = { Text(opcao, fontSize = 10.sp, maxLines = 1) }, modifier = Modifier.padding(horizontal = 3.dp),
                    colors = FilterChipDefaults.filterChipColors(containerColor = Color.White,
                        selectedContainerColor = Color(0xFFFFE4D5)))
            }
        }
        Spacer(Modifier.height(10.dp))
        Text("${categoria} • ${filtrados.size} resultado(s)", color = Color.White, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(10.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 26.dp)) {
            if (filtrados.isEmpty()) item { Text("Nenhum resultado encontrado.", color = Color.White) }
            items(filtrados, key = { "explorar_${it.id}" }) { lugar ->
                Card(onClick = { onSelecionarDestino(lugar.id) }, shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column {
                        Image(painterResource(lugar.foto), contentDescription = lugar.nome,
                            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth().height(135.dp))
                        Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(lugar.nome, fontWeight = FontWeight.Bold, fontSize = 16.sp,
                                    modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                                IconButton(onClick = {
                                    if (lugar.id in dados.lugaresSalvos) dados.lugaresSalvos.remove(lugar.id)
                                    else dados.lugaresSalvos.add(lugar.id)
                                }) {
                                    Icon(if (lugar.id in dados.lugaresSalvos) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                                        "Salvar lugar", tint = corPrincipal)
                                }
                            }
                            Text(lugar.regiao, fontSize = 12.sp, color = Color.Gray)
                            Text(lugar.descricao, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (lugar.nota != "—") {
                                    Icon(Icons.Outlined.Star, null, tint = Color(0xFFFFA000), modifier = Modifier.size(17.dp))
                                    Text(" ${lugar.nota}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Spacer(Modifier.weight(1f))
                                Text("Ver avaliações  ›", fontSize = 12.sp, color = corPrincipal)
                            }
                        }
                    }
                }
            }
        }
    }
}
