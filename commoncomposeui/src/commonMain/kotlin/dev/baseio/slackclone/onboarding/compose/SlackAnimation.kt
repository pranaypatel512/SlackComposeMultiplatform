package dev.baseio.slackclone.onboarding.compose

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.baseio.slackclone.commonui.theme.SlackCloneTypography
import dev.baseio.slackclone.onboarding.SlackAnim
import dev.baseio.slackclone.onboarding.SlackAnim.ANIM_DURATION

val loaderYellow = Color(226, 179, 75)
val loaderPinkish = Color(206, 54, 92)
val loaderBluish = Color(100, 193, 234)
val loaderGreen = Color(91, 178, 128)
val slackWhite = Color(255, 255, 255)

@Composable
internal fun SlackAnimation(isStartAnimation: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val animatedRotateLogo by animateFloatAsState(
            targetValue = if (isStartAnimation) 0f else 360f,
            tween(durationMillis = SlackAnim.ANIM_DURATION)
        )

        val animatedMoveLogo by animateDpAsState(
            targetValue = if (isStartAnimation) (-120).dp else 0.dp,
            tween(SlackAnim.ANIM_DURATION)
        )

        SKTextLoader(Modifier.align(Alignment.Center), isStartAnimation)

        SKFourColorLoader(
            Modifier.align(Alignment.Center),
            animatedRotateLogo,
            animatedMoveLogo,
            isStartAnimation
        )
    }
}

@Composable
internal fun SKFourColorLoader(
    modifier: Modifier,
    animatedRotateLogo: Float,
    animatedMoveLogo: Dp,
    isStartAnimation: Boolean
) {
    Box(
        modifier = modifier
            .offset(x = animatedMoveLogo)
            .rotate(animatedRotateLogo)
            .scale(1.3f)
    ) {
        SlackAnimSpec.blocks.forEach { block ->
            CircularRectBlock(
                block,
                isStartAnimation
            )
        }
    }
}

@Composable
internal fun CircularRectBlock(block: SlackAnimSpec.CircularRectBlockData, isMoveLeft: Boolean) {
    val dropScale by animateFloatAsState(
        targetValue = if (isMoveLeft) 1f else 0f,
        SlackAnimSpec.dropSizeKeyFrames(block.dropScaleDelay, isMoveLeft)
    )

    val animatedWidth by animateDpAsState(
        targetValue = if (isMoveLeft) block.rectBlockWidth else block.rectBlockHeight,
        tween(durationMillis = SlackAnim.ANIM_DURATION.times(2))
    )

    Droplet(block, dropScale)

    Block(block, animatedWidth)
}

@Composable
internal fun Block(
    block: SlackAnimSpec.CircularRectBlockData,
    animatedWidth: Dp
) {
    Box(
        modifier = Modifier
            .offset(block.offsetX, block.offsetY)
            .rotate(block.dropRotation.toFloat())
            .width(animatedWidth) // change this to animatedWidth later
            .height(block.rectBlockHeight)
            .background(block.color, RoundedCornerShape(block.roundedCornerPercentage))
    )
}

@Composable
internal fun Droplet(block: SlackAnimSpec.CircularRectBlockData, dropSize: Float) {
    Box(
        Modifier
            .offset(block.dropX, block.dropY)
            .rotate(block.dropRotation.plus(180f))
            .size(block.dropSize)
            .scale(dropSize)
            .background(
                block.color,
                RoundedCornerShape(
                    topStartPercent = block.roundedCornerPercentage,
                    topEndPercent = block.roundedCornerPercentage,
                    bottomStartPercent = block.roundedCornerPercentage,
                    bottomEndPercent = 0
                )
            )
    )
}

@Composable
internal fun SKTextLoader(modifier: Modifier, isStartAnimation: Boolean) {
    Box(
        modifier = modifier.offset(x = 40.dp)
    ) {
        Row {
            AnimatedLetter("s", isStartAnimation, delay = SlackAnim.ANIM_DURATION.times(0.6).toInt())
            AnimatedLetter("l", isStartAnimation, delay = SlackAnim.ANIM_DURATION.times(0.3).toInt())
            AnimatedLetter("a", isStartAnimation, delay = SlackAnim.ANIM_DURATION.times(0.2).toInt())
            AnimatedLetter("c", isStartAnimation, delay = SlackAnim.ANIM_DURATION.times(0.1).toInt())
            AnimatedLetter("k", isStartAnimation, delay = 0)
        }
    }
}

@Composable
internal fun AnimatedLetter(letter: String, isVisible: Boolean, delay: Int) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        tween(durationMillis = SlackAnim.ANIM_DURATION.div(2), delayMillis = delay)
    )
    Text(
        text = letter,
        fontWeight = FontWeight.Bold,
        style = slackTextStyle(),
        modifier = Modifier.alpha(animatedAlpha)
    )
}

@Composable
internal fun slackTextStyle() = SlackCloneTypography.h2.copy(
    color = slackWhite,
    letterSpacing = 4.sp
)

object SlackAnimSpec {

    data class CircularRectBlockData(
        val rectBlockWidth: Dp = 32.dp,
        val rectBlockHeight: Dp = 8.dp,
        val roundedCornerPercentage: Int = 30,
        val color: Color,
        val rotation: Int,
        var offsetX: Dp = 0.dp,
        var offsetY: Dp = 0.dp,
        var dropX: Dp = 0.dp,
        var dropY: Dp = 0.dp,
        val dropRotation: Int,
        val dropSize: Dp = 16.dp,
        val dropScaleDelay: Int
    )

    fun dropSizeKeyFrames(scaleDelay: Int, isMoveLeft: Boolean): AnimationSpec<Float> {
        if (isMoveLeft) {
            return keyframes {
                durationMillis = ANIM_DURATION
                delayMillis = scaleDelay
                0f at 0 with LinearEasing
                1.3f at ANIM_DURATION.div(2) with LinearEasing
                1f at ANIM_DURATION with LinearEasing
            }
        } else {
            return keyframes {
                durationMillis = ANIM_DURATION
                delayMillis = scaleDelay
                1f at 0 with LinearEasing
                1.3f at ANIM_DURATION.div(2) with LinearEasing
                0f at ANIM_DURATION with LinearEasing
            }
        }
    }

    val blocks = mutableListOf<CircularRectBlockData>().apply {

        add(
            CircularRectBlockData(
                rectBlockWidth = 34.dp,
                rectBlockHeight = 16.dp,
                roundedCornerPercentage = 50,
                color = loaderYellow,
                rotation = 0,
                offsetY = 10.dp,
                offsetX = 16.dp,
                dropY = 32.dp,
                dropX = 16.dp,
                dropRotation = 0, dropScaleDelay = 300
            )
        )

        add(
            CircularRectBlockData(
                rectBlockWidth = 34.dp,
                rectBlockHeight = 16.dp,
                roundedCornerPercentage = 50,
                color = loaderPinkish,
                rotation = 90,
                offsetX = (-14).dp,
                offsetY = 20.dp,
                dropX = (-24).dp,
                dropY = (10).dp,
                dropRotation = 90, dropScaleDelay = 200
            )
        )
        add(
            CircularRectBlockData(
                rectBlockWidth = 34.dp,
                rectBlockHeight = 16.dp,
                roundedCornerPercentage = 50,
                color = loaderBluish,
                rotation = 180,
                offsetX = (-24).dp,
                offsetY = (-12).dp,
                dropX = (-10).dp,
                dropY = (-36).dp,
                dropRotation = 180, dropScaleDelay = 0
            )
        )

        add(
            CircularRectBlockData(
                rectBlockWidth = 34.dp,
                rectBlockHeight = 16.dp,
                roundedCornerPercentage = 50,
                color = loaderGreen,
                rotation = 270,
                offsetX = 6.dp,
                offsetY = (-24).dp,
                dropX = (36).dp,
                dropY = (-16).dp,
                dropRotation = 270, dropScaleDelay = 100
            )
        )
    }
}
