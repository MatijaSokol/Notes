package com.matijasokol.notes.ui.components

import com.matijasokol.notes.ui.components.ToastDuration.LONG
import com.matijasokol.notes.ui.components.ToastDuration.SHORT
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UILabel
import platform.UIKit.UIScreen
import platform.UIKit.UIView
import platform.UIKit.UIViewAnimationOptionCurveEaseOut

@OptIn(ExperimentalForeignApi::class)
class ToastIos : Toast {

    override fun show(message: String, duration: ToastDuration) {
        val animationDuration = when (duration) {
            SHORT -> 6.0
            LONG -> 10.0
        }

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        val toastLabel = UILabel(
            frame = CGRectMake(
                x = 0.0,
                y = 0.0,
                width = UIScreen.mainScreen.bounds.useContents { size.width } - 40,
                height = 35.0,
            ),
        ).apply {
            center = CGPointMake(
                x = UIScreen.mainScreen.bounds.useContents { size.width } / 2,
                y = UIScreen.mainScreen.bounds.useContents { size.height } - 100.0,
            )
            textAlignment = NSTextAlignmentCenter
            backgroundColor = UIColor.blackColor.colorWithAlphaComponent(0.6)
            textColor = UIColor.whiteColor
            text = message
            alpha = 1.0
            layer.cornerRadius = 15.0
            clipsToBounds = true
        }

        rootViewController?.view?.addSubview(toastLabel)

        UIView.animateWithDuration(
            duration = animationDuration,
            delay = 0.1,
            options = UIViewAnimationOptionCurveEaseOut,
            animations = { toastLabel.alpha = 0.0 },
            completion = { completed ->
                if (completed) {
                    toastLabel.removeFromSuperview()
                }
            },
        )
    }
}
