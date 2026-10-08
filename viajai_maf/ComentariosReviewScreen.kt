package com.example.viajai_maf

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private val laranjaReview = Color(0xFFE87735)
private val vermelhoReview = Color(0xFF8B261D)
private val ouro = Color(0xFFFFA900)
data class Comentario(val id: Int, val autor: String, val texto: String, val nota: Double, val curtidas: Int = 0, val curtiu: Boolean = false)

@Composable
fun ComentariosReviewScreen(pousadaId: Int, onVoltar: () -> Unit, dados: ViajaiDados? = null) {
    val comentarios = remember(pousadaId) { mutableStateListOf(
        Comentario(1, "Lucas M.", "Lugar excelente, ótimo atendimento!", 5.0),
        Comentario(2, "Ana P.", "Muito limpo, café da manhã maravilhoso.", 5.0),
        Comentario(3, "Carlos R.", "Boa localização, mas o Wi-Fi oscilou.", 4.0)
    ) }
    var autor by remember { mutableStateOf("") }
    var texto by remember { mutableStateOf("") }
    var nota by remember { mutableDoubleStateOf(5.0) }
    val media = comentarios.map { it.nota }.average().takeIf { !it.isNaN() } ?: 0.0
    Column(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(laranjaReview, vermelhoReview)))) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVoltar) { Icon(Icons.Default.ArrowBack, "Voltar", tint = Color.White) }
            Text("Avaliações e Comentários", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        LazyColumn(Modifier.fillMaxSize().padding(horizontal = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 28.dp)) {
            item { Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = ouro, modifier = Modifier.size(38.dp)); Spacer(Modifier.width(12.dp))
                    Column { Text("Média: ${String.format(Locale.US, "%.1f", media)} / 5,0", color = vermelhoReview, fontWeight = FontWeight.Bold); Text("Baseado em ${comentarios.size} avaliações", style = MaterialTheme.typography.bodySmall) }
                }
            } }
            item { Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Deixe seu comentário", fontWeight = FontWeight.Bold)
                    Text("Sua nota: ${String.format(Locale.US, "%.1f", nota)} / 5,0", color = vermelhoReview)
                    Slider(value = nota.toFloat(), onValueChange = { nota = it.toDouble() }, valueRange = 0.5f..5f, steps = 8)
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) { (1..5).forEach { estrela ->
                        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Icon(if (nota >= estrela) Icons.Default.Star else Icons.Default.StarBorder, null, tint = ouro)
                        }
                    } }
                    OutlinedTextField(autor, { autor = it }, label = { Text("Seu nome") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(texto, { texto = it }, label = { Text("Escreva sobre o lugar") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                    Button(onClick = { comentarios.add(0, Comentario((comentarios.maxOfOrNull { it.id } ?: 0) + 1, autor.trim(), texto.trim(), nota)); autor = ""; texto = ""; nota = 5.0 }, enabled = autor.isNotBlank() && texto.isNotBlank(), modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = laranjaReview)) { Text("Publicar avaliação") }
                }
            } }
            items(comentarios, key = { "review_${it.id}" }) { comentario ->
                Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(14.dp)) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = CircleShape, color = Color(0xFFFFE5D9), modifier = Modifier.size(40.dp)) { Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, "Foto de perfil", tint = vermelhoReview) } }
                            Spacer(Modifier.width(9.dp))
                            Column(Modifier.weight(1f)) { Text(comentario.autor, fontWeight = FontWeight.Bold); Text("${comentario.nota} / 5 estrelas", color = ouro, fontSize = 12.sp) }
                            Icon(Icons.Default.Star, null, tint = ouro)
                        }
                        Text(comentario.texto)
                        TextButton(onClick = { val i = comentarios.indexOfFirst { it.id == comentario.id }; if (i >= 0) comentarios[i] = comentario.copy(curtidas = comentario.curtidas + if (comentario.curtiu) -1 else 1, curtiu = !comentario.curtiu) }) {
                            Icon(if (comentario.curtiu) Icons.Default.Favorite else Icons.Default.FavoriteBorder, "Curtir"); Spacer(Modifier.width(5.dp)); Text("Curtir (${comentario.curtidas})")
                        }
                    }
                }
            }
        }
    }
}
