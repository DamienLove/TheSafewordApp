package com.safeword.ui.newui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun UpgradeScreen(modifier: Modifier = Modifier) {
    val features = listOf(
        "WorkingDnelits" to "Lead prorethendvon flesliling inturle: Pori",
        "Greating hist" to "Lead prorethondally aesilling inturle: Porl",
        "Deathroight" to "Lead protected vip flesiting inturle: Pofi",
        "Fuse goofl" to "Lead prorethondally aesillurg inturle: Pofl"
    )
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
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "SafeWord Pro",
            style = NewUiTypography.headingXL,
            color = NewUiColors.onBackground
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Upgrade",
            style = NewUiTypography.headingLarge,
            color = NewUiColors.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            colors = CardDefaults.cardColors(containerColor = NewUiColors.surfaceElevated),
            shape = RoundedCornerShape(28.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NewUiColors.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Shield Artwork",
                    color = NewUiColors.onSurfaceMuted,
                    style = NewUiTypography.bodyMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f, fill = true)
        ) {
            features.forEach { feature ->
                FeatureRow(title = feature.first, description = feature.second)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NewUiColors.accentRed,
                contentColor = Color.White
            ),
            shape = MaterialTheme.shapes.large
        ) {
            Text(text = "Premium features", style = NewUiTypography.bodyLarge)
        }
    }
}

@Composable
private fun FeatureRow(title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NewUiColors.surfaceElevated),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(NewUiColors.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "★",
                    color = NewUiColors.accentGold,
                    style = NewUiTypography.bodyLarge
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = NewUiColors.onBackground, style = NewUiTypography.bodyLarge)
                Text(text = description, color = NewUiColors.onSurfaceMuted, style = NewUiTypography.bodyMedium)
            }
            Icon(imageVector = Icons.Default.HelpOutline, contentDescription = null, tint = NewUiColors.onSurfaceMuted)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun UpgradeScreenPreview() {
    NewUiTheme {
        Surface {
            UpgradeScreen()
        }
    }
}
