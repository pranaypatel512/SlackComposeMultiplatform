package dev.baseio.slackclone.commonui.reusable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import dev.baseio.slackclone.commonui.theme.SlackCloneColorProvider
import dev.baseio.slackclone.commonui.theme.SlackCloneSurface

@Composable
fun SlackOnlineBox(imageUrl: String,
                   parentModifier: Modifier = Modifier.size(34.dp),
                   imageModifier:Modifier = Modifier.size(28.dp)) {
  ConstraintLayout(parentModifier) {
    val (image, indicator) = createRefs()
    SlackImageBox(
      imageModifier
        .constrainAs(image) {
          top.linkTo(parent.top)
          bottom.linkTo(parent.bottom)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
        }, imageUrl
    )
    SlackCloneSurface(shape = CircleShape,
      border = BorderStroke(3.dp, color = SlackCloneColorProvider.colors.uiBackground),
      modifier = Modifier
        .constrainAs(indicator) {
          bottom.linkTo(parent.bottom)
          end.linkTo(parent.end)
        }
        .size(14.dp)){
      Box(
        modifier = Modifier
          .size(12.dp)
          .clip(CircleShape)
          .background(Color.Green)

      )
    }

  }
}