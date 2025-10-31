package com.safeword.ui.newui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.safeword.ui.design.NewUiColors
import com.safeword.ui.design.NewUiDimensions
import com.safeword.ui.design.NewUiTheme
import com.safeword.ui.design.NewUiTypography

@Composable
fun SetupChecklistScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NewUiColors.background)
            .padding(NewUiDimensions.screenPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Map,
                contentDescription = null,
                tint = NewUiColors.onSurfaceMuted,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.HelpOutline,
                contentDescription = "Help",
                tint = NewUiColors.accentGold
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Setup",
            style = NewUiTypography.headingXL,
            color = NewUiColors.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            shape = MaterialTheme.shapes.extraLarge,
            colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = NewUiColors.surfaceElevated)
        ) {
            // Placeholder map illustration block
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NewUiColors.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = null,
                    tint = NewUiColors.accentRed,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(28.dp))
        val steps = listOf(
            "Location services" to true,
            "Microphone" to true,
            "SMS" to true,
            "Phone" to true,
            "Do Not Disturb" to false,
            "Battery Unrestricted" to false
        )
        steps.forEachIndexed { index, step ->
            SetupStepRow(step.first, step.second)
            if (index != steps.lastIndex) {
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NewUiColors.accentGold,
                contentColor = Color.Black
            ),
            shape = MaterialTheme.shapes.large
        ) {
            Text(text = "Finished", style = NewUiTypography.bodyLarge)
        }
    }
}

@Composable
private fun SetupStepRow(label: String, isComplete: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(if (isComplete) NewUiColors.success else NewUiColors.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (isComplete) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = NewUiTypography.bodyLarge,
            color = if (isComplete) NewUiColors.onBackground else NewUiColors.onSurfaceMuted
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun SetupChecklistPreview() {
    NewUiTheme {
        Surface {
            SetupChecklistScreen()
        }
    }
}
