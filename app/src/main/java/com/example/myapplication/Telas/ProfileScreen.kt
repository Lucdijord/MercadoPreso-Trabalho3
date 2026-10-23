package com.example.myapplication.Telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.myapplication.R

@Composable
fun ProfileScreen(
    onEnderecosClick: () -> Unit,
    onCatalogoClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo_mp),
            contentDescription = "Logo App MP",
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Meu Perfil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Bem-vindo ao App MP",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    "Minha Conta",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                HorizontalDivider()

                Button(
                    onClick = onEnderecosClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F)
                    )
                ) {
                    Text("Meus Endereços")
                }

                OutlinedButton(
                    onClick = onCatalogoClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Catálogo")
                }
            }
        }
    }
}
