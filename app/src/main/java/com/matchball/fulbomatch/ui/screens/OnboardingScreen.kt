package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matchball.fulbomatch.R
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.theme.White
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: Int,
    val imageDescription: String
)

@Composable
fun OnboardingScreen(onStartClick: () -> Unit) {

    val pages = listOf(
        OnboardingPage(
            title = "Encontrá tu próximo partido",
            description = "Explorá el mapa interactivo y sumate a partidos cerca de tu ubicación. Nunca te quedes sin jugar.",
            imageRes = R.drawable.pelota1,
            imageDescription = "Jugadores en una cancha de fútbol"
        ),
        OnboardingPage(
            title = "Armá tu equipo",
            description = "Creá partidos, invitá a tus amigos y organizá encuentros en segundos. El fútbol amateur nunca fue tan fácil de gestionar.",
            imageRes = R.drawable.equipo,
            imageDescription = "Equipo de fútbol reunido"
        ),
        OnboardingPage(
            title = "Sumate a la comunidad",
            description = "Encontrá jugadores de tu nivel, hacé amigos y disfrutá de la pasión del fútbol. El partido ideal te está esperando.",
            imageRes = R.drawable.comunidad,
            imageDescription = "Comunidad de jugadores"
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(24.dp)
            .semantics { contentDescription = "Pantalla de bienvenida" },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo_completo),
            contentDescription = "Logo FulboMatch",
            modifier = Modifier
                .padding(top = 16.dp)
                .height(48.dp)
                .wrapContentWidth(),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageIndex ->
            OnboardingPageContent(page = pages[pageIndex])
        }

        // Dots indicadores
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .semantics {
                    contentDescription =
                        "Página ${pagerState.currentPage + 1} de ${pages.size}"
                },
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (index == pagerState.currentPage) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == pagerState.currentPage) GreenPrimary
                            else GreenPrimary.copy(alpha = 0.3f)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón Siguiente / Empezar
        Button(
            onClick = {
                if (pagerState.currentPage < pages.size - 1) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    onStartClick()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
        ) {
            Text(
                text = if (pagerState.currentPage < pages.size - 1) "Siguiente →" else "Empezar →",
                fontSize = 16.sp,
                color = White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Imagen real
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = page.imageDescription,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = page.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = page.description,
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}