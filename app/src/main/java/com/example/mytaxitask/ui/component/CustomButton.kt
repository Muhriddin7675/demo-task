
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mytaxitask.ui.component.CustomTextView
import com.example.mytaxitask.ui.theme.black

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    textFontSize: Int,
    onClick: () -> Unit,
    enable: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    enableBackgroundColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    padding: Int = 4,
    cornerRadius: Int = 10,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius.dp)) // Shaklni belgilash
            .clickable(onClick = onClick)
            .background(
                color = if (enable) enableBackgroundColor else backgroundColor,
            )
            .fillMaxHeight(),
        contentAlignment = Alignment.Center

    ) {
        CustomTextView(
            text = text,
            color = if (!enable) contentColor else black,
            fontSize = textFontSize,
            fontWeight = if(enable) 700 else 400,
        )
    }
}
