package com.example.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Full-screen background displaying a real-life golf course photograph
 * with a high-contrast dark gradient scrim for exceptional readability.
 */
@Composable
fun GolfBackground(
    modifier: Modifier = Modifier,
    scrimAlpha: Float = 0.82f,
    content: @Composable BoxScope.() -> Unit
) {
    GolfBackgroundWrapper(modifier = modifier, useBannerImage = false, content = content)
}
