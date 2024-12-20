package com.example.christmastreeanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframesWithSpline
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.christmastreeanimation.ui.theme.ChristmasAnimationTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChristmasAnimationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChristmasScreen()
                }
            }
        }
    }
}

@Composable
fun ChristmasScreen() {
    val background = painterResource(id = R.drawable.christmas_background)

    SnowAnimation(
        modifier = Modifier.fillMaxSize(),
        background = background
    )
}

@OptIn(ExperimentalAnimationSpecApi::class)
@Composable
fun SnowAnimation(
    modifier: Modifier = Modifier,
    background: Painter
) {
    class Snowflake(
        val initialX: Float,
        val delayMillis: Int
    )

    val snowflakes = remember {
        List(30) {
            Snowflake(
                initialX = Random.nextFloat(),
                delayMillis = Random.nextInt(5000)
            )
        }
    }

    var containerWidth by remember { mutableStateOf(0) }
    var containerHeight by remember { mutableStateOf(0) }

    Box(modifier = modifier.onSizeChanged {
        containerWidth = it.width
        containerHeight = it.height
    }) {
        Image(
            painter = background,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        snowflakes.forEach { snowflake ->

            var yOffset by remember { mutableFloatStateOf(-50f - snowflake.delayMillis / 10f) }

            LaunchedEffect(containerHeight) {
                if (containerHeight > 0) {
                    delay(snowflake.delayMillis.toLong())

                    animate(
                        initialValue = -50f - snowflake.delayMillis / 10f,
                        targetValue = containerHeight.toFloat(),
                        animationSpec = infiniteRepeatable(
                            animation = keyframesWithSpline {
                                durationMillis = 5000
                            }
                        )
                    ) { value, _ -> yOffset = value }
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.icon_flake),
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier
                    .size(24.dp)
                    .offset {
                        IntOffset(
                            x = (snowflake.initialX * containerWidth).roundToInt(),
                            y = yOffset.roundToInt()
                        )
                    }
            )
        }
    }
}