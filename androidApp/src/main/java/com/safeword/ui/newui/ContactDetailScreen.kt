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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
fun NewContactDetailScreen(modifier: Modifier = Modifier) {
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
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = NewUiColors.surfaceElevated)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(NewUiColors.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CA",
                        style = NewUiTypography.headingLarge,
                        color = NewUiColors.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Claudia Alves",
                    style = NewUiTypography.headingLarge,
                    color = NewUiColors.onBackground
                )
                Text(
                    text = "Linked",
                    style = NewUiTypography.bodyMedium,
                    color = NewUiColors.success
                )
            }
        }
        Spacer(modifier = Modifier.height(28.dp))
        val detailRows = listOf(
            DetailRowItem("hello@reallygreatsite.com", "E-mail Address", Icons.Default.MailOutline),
            DetailRowItem("+123-456-7890", "Phone Number", Icons.Default.Phone),
            DetailRowItem("Location", "Share status", Icons.Default.LocationOn)
        )
        detailRows.forEachIndexed { index, item ->
            DetailRow(item)
            if (index != detailRows.lastIndex) {
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        Text(
            text = "Contact Settings",
            style = NewUiTypography.bodyLarge,
            color = NewUiColors.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        SettingToggleRow("Location Share", true)
        Spacer(modifier = Modifier.height(14.dp))
        SettingToggleRow("Emergency Alert", false)
        Spacer(modifier = Modifier.height(14.dp))
        SettingToggleRow("Non-Emergency Alert", false)
        Spacer(modifier = Modifier.height(14.dp))
        SettingToggleRow("Camera Enable", false, inactiveColor = NewUiColors.accentRed)
        Spacer(modifier = Modifier.height(14.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = NewUiColors.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Emergency Contact Order",
                        color = NewUiColors.onBackground,
                        style = NewUiTypography.bodyLarge
                    )
                    Text(
                        text = "Tap to reorder",
                        color = NewUiColors.onSurfaceMuted,
                        style = NewUiTypography.bodyMedium
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(NewUiColors.accentGold),
                    contentAlignment = Alignment.Center
                ) {
                    Text("2", color = Color.Black, style = NewUiTypography.bodyLarge)
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Sign Out",
                color = NewUiColors.accentRed,
                style = NewUiTypography.bodyMedium
            )
            Text(
                text = "Privacy & Policy",
                color = NewUiColors.onSurfaceMuted,
                style = NewUiTypography.bodyMedium
            )
        }
    }
}

private data class DetailRowItem(
    val value: String,
    val subtitle: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
private fun DetailRow(item: DetailRowItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = NewUiColors.surfaceElevated)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = NewUiColors.primary
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.value, color = NewUiColors.onBackground, style = NewUiTypography.bodyLarge)
                Text(text = item.subtitle, color = NewUiColors.onSurfaceMuted, style = NewUiTypography.bodyMedium)
            }
            Icon(imageVector = Icons.Default.HelpOutline, contentDescription = null, tint = NewUiColors.onSurfaceMuted)
        }
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    checked: Boolean,
    inactiveColor: Color = NewUiColors.surfaceVariant
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NewUiColors.surfaceElevated, RoundedCornerShape(18.dp))
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = NewUiTypography.bodyLarge,
            color = NewUiColors.onBackground
        )
        Switch(
            checked = checked,
            onCheckedChange = {},
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = NewUiColors.primary,
                uncheckedThumbColor = Color.Black,
                uncheckedTrackColor = inactiveColor
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun ContactDetailPreview() {
    NewUiTheme {
        Surface {
            NewContactDetailScreen()
        }
    }
}
