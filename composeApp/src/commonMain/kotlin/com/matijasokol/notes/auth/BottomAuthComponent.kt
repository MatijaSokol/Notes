package com.matijasokol.notes.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matijasokol.notes.ui.components.withDarkRipple
import notes.composeapp.generated.resources.Res
import notes.composeapp.generated.resources.icon_google
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomAuthComponent(
    buttonText: String,
    spacerText: String,
    bottomText: String,
    onButtonClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onBottomTextClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = onButtonClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color.Transparent),
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF9DCEFF),
                                    Color(0xFFC58BF2),
                                ),
                            ),
                            shape = RoundedCornerShape(50.dp),
                        )
                        .fillMaxWidth()
                        .heightIn(48.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = buttonText,
                        color = Color.White,
                        fontSize = 20.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    thickness = 1.dp,
                    color = Color.Gray,
                )

                Text(
                    text = spacerText,
                    modifier = Modifier.padding(10.dp),
                    fontSize = 20.sp,
                )

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    thickness = 1.dp,
                    color = Color.Gray,
                )
            }

            withDarkRipple {
                Button(
                    onClick = onGoogleClick,
                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                    modifier = Modifier
                        .padding(4.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(20.dp),
                        ),
                ) {
                    Image(
                        painter = painterResource(resource = Res.drawable.icon_google),
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(30.dp),
                    )
                }
            }

            Text(
                text = bottomText,
                modifier = Modifier.padding(10.dp).clickable(onClick = onBottomTextClick),
                fontSize = 20.sp,
            )
        }
    }
}
