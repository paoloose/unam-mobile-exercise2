package site.paoloose.unam.exercise1.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import site.paoloose.unam.exercise1.R

// An interested trick that I learnt
// It is not possible to use a component as the splash screen for your android app
// So... you need to ditch the native splash, and pass directly to your component splash
// But native splash can't be skipped, but... we can modify it to be the first frame of our animation

// That's what I did, see app/src/main/res/values/themes.xml, and you'll realize that the native
// splash screen is just a solid color (darkGreen), that is exactly the first keyframe of this animated component

@Composable
fun AnimatedSplashScreen(onSplashFinished: () -> Unit) {
    val logoScale = remember { Animatable(0f) }
    val logoAlpha = remember { Animatable(0f) }

    val textAlpha = remember { Animatable(0f) }
    val textOffset = remember { Animatable(20f) }

    // https://developer.android.com/develop/ui/compose/side-effects#launchedeffect
    // The "splash" animation, that runs in a detached (suspend) coroutine
    LaunchedEffect(key1 = true) {
        // 1. Pop in from 0 to 1f (both scale and alpha)
        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800)
            )
        }

        // 2. we wait slightly for logo to appear, then fade in the text rising up
        delay(600)
        launch {
            textAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }
        launch {
            textOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }

        // And some suspense....
        delay(1500L)

        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(site.paoloose.unam.exercise1.ui.theme.GreenDark), // Match native splash screen
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.world_cup_logo),
                contentDescription = stringResource(R.string.cd_splash_logo),
                modifier = Modifier
                    .size(320.dp)
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.splash_title),
                color = Color.White,
                style = MaterialTheme.typography.displayMedium, // Increased from displaySmall to displayMedium
                fontWeight = FontWeight.ExtraBold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .offset(y = textOffset.value.dp)
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.splash_subtitle),
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .offset(y = textOffset.value.dp)
                    .padding(horizontal = 24.dp)
            )
        }
    }
}
