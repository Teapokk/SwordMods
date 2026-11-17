package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.InnerslothSession
import com.example.ui.components.CrewmateAvatar
import com.example.ui.theme.SlateBackground
import com.example.ui.theme.SlateCardBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.SwordEmerald
import com.example.ui.theme.SwordGold
import com.example.ui.theme.SwordRed
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextMutedSlate
import com.example.ui.theme.TextWhite

@Composable
fun AccountScreen(
    session: InnerslothSession,
    onUpdateSession: (InnerslothSession) -> Unit,
    onRenewSession: () -> Unit,
    onLogout: () -> Unit,
    onOpenInnerslothPortal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "Conta Oficial Innersloth & Cosméticos",
                color = TextWhite,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp
            )
            Text(
                text = "Autenticação persistente com sessão estendida de 24 horas.",
                color = TextGray,
                fontSize = 12.sp
            )
        }

        // Live Preview Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SwordRed.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF141924))
                        .border(1.dp, SlateCardBorder, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    CrewmateAvatar(
                        bodyColor = Color(session.selectedColorValue),
                        size = 64.dp
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = session.accountName,
                        color = TextWhite,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                    Text(
                        text = session.friendCode,
                        color = SwordGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Cor: ${session.selectedColorName} • Traje: ${session.selectedSkin}",
                        color = TextGray,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Innersloth 24-Hour Session Status Card (User requested: "não dure uma hora pelo menos até 24 horas")
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (session.isSessionValid) SwordEmerald.copy(alpha = 0.6f) else SwordRed.copy(alpha = 0.6f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LockClock,
                            contentDescription = null,
                            tint = if (session.isSessionValid) SwordEmerald else SwordRed,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Duração da Sessão de Login",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (session.isSessionValid) SwordEmerald.copy(alpha = 0.2f) else SwordRed.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (session.isSessionValid) "24h ATIVA" else "EXPIRADA",
                            color = if (session.isSessionValid) SwordEmerald else SwordRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF131822))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "Tempo restante: ${session.formattedRemainingTime}",
                            color = SwordGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Token de autenticação configurado para durar 24 horas completas, impedindo desconexões durante partidas longas.",
                            color = TextMutedSlate,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onRenewSession,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SwordGold,
                            contentColor = Color(0xFF14171E)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Renovar (+24h)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onLogout,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SwordRed),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SwordRed.copy(alpha = 0.5f))
                    ) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sair", fontSize = 12.sp)
                    }
                }
            }
        }

        // Official Innersloth Login Portal Link
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Portal Oficial Innersloth Accounts",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "Acesse accounts.innersloth.com para vincular sua conta Google e salvar suas roupas oficiais.",
                    color = TextGray,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onOpenInnerslothPortal,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SwordRed,
                        contentColor = TextWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Abrir accounts.innersloth.com", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        // Player Name & Friend Code Settings
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = session.accountName,
                    onValueChange = { onUpdateSession(session.copy(accountName = it)) },
                    label = { Text("Nome de Jogador (Nickname)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SwordRed,
                        unfocusedBorderColor = SlateCardBorder,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = session.friendCode,
                    onValueChange = { onUpdateSession(session.copy(friendCode = it)) },
                    label = { Text("Friend Code Innersloth (ex: NICK#1234)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SwordGold,
                        unfocusedBorderColor = SlateCardBorder,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Auto-Renovação de Sessão (24 Horas)",
                            color = TextWhite,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Renova automaticamente a cada 24h para você nunca ser deslogado",
                            color = TextMutedSlate,
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = session.autoRenew,
                        onCheckedChange = { onUpdateSession(session.copy(autoRenew = it)) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TextWhite,
                            checkedTrackColor = SwordEmerald,
                            uncheckedThumbColor = TextMutedSlate,
                            uncheckedTrackColor = SlateSurfaceVariant
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Preservar Roupas & Cosméticos",
                            color = TextWhite,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Mantém seus visuais salvos na memória local do mod",
                            color = TextMutedSlate,
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = session.keepCosmetics,
                        onCheckedChange = { onUpdateSession(session.copy(keepCosmetics = it)) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TextWhite,
                            checkedTrackColor = SwordRed,
                            uncheckedThumbColor = TextMutedSlate,
                            uncheckedTrackColor = SlateSurfaceVariant
                        )
                    )
                }
            }
        }

        // Color Selector
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Cor Oficial do Tripulante",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                val colorOptions = listOf(
                    Pair("Vermelho", 0xFFE53935),
                    Pair("Azul", 0xFF132ED1),
                    Pair("Verde", 0xFF117F2D),
                    Pair("Rosa", 0xFFED54BA),
                    Pair("Laranja", 0xFFEF7D0D),
                    Pair("Amarelo", 0xFFF5F557),
                    Pair("Preto", 0xFF3F474E),
                    Pair("Branco", 0xFFD6E0F0),
                    Pair("Roxo", 0xFF6B2FBC),
                    Pair("Marrom", 0xFF71491E),
                    Pair("Ciano", 0xFF38FEDC),
                    Pair("Lima", 0xFF50EF39)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(colorOptions) { (name, hex) ->
                        val isSelected = session.selectedColorValue == hex
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(hex))
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) Color.White else Color(0x44000000),
                                    shape = CircleShape
                                )
                                .clickable {
                                    onUpdateSession(
                                        session.copy(
                                            selectedColorName = name,
                                            selectedColorValue = hex
                                        )
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
