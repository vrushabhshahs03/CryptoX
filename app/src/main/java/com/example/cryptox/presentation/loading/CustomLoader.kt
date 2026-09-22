package com.example.cryptox.presentation.loading

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CustomLoaderSameCircle(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 180f,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2200,
                easing = LinearEasing
            )
        )
    )

    Canvas(
        modifier = modifier.size(250.dp)
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val ovalWidth = 30f
        val ovalHeight = 200f
        fun DrawScope.drawOrbit(
            rotation: Float,
            offsets: List<Float>,
        ) {
            rotate(
                degrees = rotation,
                pivot = Offset(centerX, centerY)
            ) {
                drawOval(
                    color = Color.Black,
                    topLeft = Offset(
                        centerX - ovalWidth / 2,
                        centerY - ovalHeight / 2
                    ),
                    size = Size(
                        width = ovalWidth,
                        height = ovalHeight
                    ),
                    style = Stroke(width = 5f)
                )

                offsets.forEach { offset ->
                    val currentAngle = angle + offset
                    val rad =
                        Math.toRadians(currentAngle.toDouble())
                    val x =
                        centerX +
                            (ovalWidth / 2) *
                            cos(rad).toFloat()
                    val y =
                        centerY +
                            (ovalHeight / 2) *
                            sin(rad).toFloat()

                    drawCircle(
                        color = Color.Black,
                        radius = 12f,
                        center = Offset(x, y)
                    )
                }
            }
        }

        drawOrbit(
            rotation = 45f,
            offsets = listOf(
                0f,
                180f
            )
        )

        drawOrbit(
            rotation = -45f,
            offsets = listOf(
                0f,
                180f
            )
        )
    }
}


@Composable
fun CustomLoader(modifier: Modifier = Modifier) {

    val infiniteTransition = rememberInfiniteTransition()

    val angle by infiniteTransition.animateFloat(
        initialValue = 180f,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2200,
                easing = LinearEasing
            )
        )
    )

    Canvas(
        modifier = modifier.size(250.dp)
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val ovalWidth = 30f
        val ovalHeight = 200f

        fun DrawScope.drawOrbit(
            rotation: Float,
            offsets: List<Float>,
        ) {
            rotate(
                degrees = rotation,
                pivot = Offset(centerX, centerY)
            ) {

                drawOval(
                    color = Color.Black,
                    topLeft = Offset(
                        centerX - ovalWidth / 2,
                        centerY - ovalHeight / 2
                    ),
                    size = Size(
                        width = ovalWidth,
                        height = ovalHeight
                    ),
                    style = Stroke(width = 5f)
                )

                offsets.forEach { offset ->

                    val currentAngle = angle + offset
                    val rad = Math.toRadians(currentAngle.toDouble())

                    val x = centerX + (ovalWidth / 2) * cos(rad).toFloat()
                    val y = centerY + (ovalHeight / 2) * sin(rad).toFloat()

                    // POSITION-BASED PULSE (IMPORTANT PART)
                    val positionPulse =
                        0.6f + 0.6f * kotlin.math.abs(kotlin.math.cos(rad)).toFloat()

                    drawCircle(
                        color = Color.Black,
                        radius = 12f * positionPulse,
                        center = Offset(x, y)
                    )
                }
            }
        }

        drawOrbit(
            rotation = 45f,
            offsets = listOf(0f, 180f)
        )

        drawOrbit(
            rotation = -45f,
            offsets = listOf(0f, 180f)
        )
    }
}