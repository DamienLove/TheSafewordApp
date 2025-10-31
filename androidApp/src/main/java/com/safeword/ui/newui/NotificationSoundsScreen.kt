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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun NotificationSoundsScreen(modifier: Modifier = Modifier) {
    val sounds = listOf("Chime (Default)", "Ascend", "Echo", "Harbor", "Orbital", "Synthwave")
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
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = NewUiColors.onBackground
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = "Help",
                    tint = NewUiColors.accentGold
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Notification Sounds",
            style = NewUiTypography.headingXL,
            color = NewUiColors.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(NewUiColors.surfaceElevated, shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp))
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Alert Tone",
                style = NewUiTypography.bodyMedium,
                color = NewUiColors.onSurfaceMuted
            )
            Text(
                text = "Chime",
                style = NewUiTypography.bodyLarge,
                color = NewUiColors.onBackground
            )
            Spacer(modifier = Modifier.height(18.dp))
            Divider(color = NewUiColors.surfaceVariant)
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Emergency Tone",
                style = NewUiTypography.bodyMedium,
                color = NewUiColors.onSurfaceMuted
            )
            Text(
                text = "Chime",
                style = NewUiTypography.bodyLarge,
                color = NewUiColors.onBackground
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Choose Alert Sound",
            style = NewUiTypography.bodyMedium,
            color = NewUiColors.onSurfaceMuted
        )
        Spacer(modifier = Modifier.height(16.dp))
        sounds.forEachIndexed { index, sound ->
            SoundRow(sound = sound, selected = index == 0)
            if (index != sounds.lastIndex) {
                Divider(color = NewUiColors.surfaceVariant, modifier = Modifier.padding(vertical = 12.dp))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            androidx.compose.material3.Button(
                onClick = {},
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = NewUiColors.surfaceVariant,
                    contentColor = NewUiColors.onBackground
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
            ) {
                Text(text = "Cancel", style = NewUiTypography.bodyLarge)
            }
            androidx.compose.material3.Button(
                onClick = {},
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = NewUiColors.primary,
                    contentColor = Color.Black
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
            ) {
                Text(text = "OK", style = NewUiTypography.bodyLarge)
            }
        }
    }
}

@Composable
private fun SoundRow(sound: String, selected: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = sound,
            style = NewUiTypography.bodyLarge,
            color = NewUiColors.onBackground,
            modifier = Modifier.weight(1f)
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(NewUiColors.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun NotificationSoundsPreview() {
    NewUiTheme {
        Surface {
            NotificationSoundsScreen()
        }
    }
}
