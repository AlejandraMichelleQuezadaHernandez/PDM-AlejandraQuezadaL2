package com.example.labo2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.labo2.ui.theme.Labo2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Labo2Theme {
                ListaNombres()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ListaNombres() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(40.dp)
        ) {
            var texto by remember { mutableStateOf("") }
            val nombres = remember { mutableStateListOf<String>() }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement =  Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ){
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = texto,
                    onValueChange = { nuevoNombre -> texto = nuevoNombre },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray,
                        unfocusedContainerColor = Color.LightGray,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color.Red
                    ),
                    label = { Text("Escribe tu nombre") }
                )
                Button(
                    onClick = {
                        if (texto.isNotBlank()) {
                            nombres.add(texto)
                            texto = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,   // color del botón
                        contentColor = Color.White     // color del texto
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ){
                    Text("Guardar")
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Listado de nombres y \nposición en la fila")
                Button(
                    onClick = { nombres.clear() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Limpiar")
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 10.dp)
                    .border(2.dp, Color.Blue)
            ) {
                itemsIndexed(nombres) { index, nombre ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = nombre)
                        Text(text = "${index + 1}")
                    }
                }
            }
        }
    }
}
