package com.creatorflow.ai.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val CreatorFlowTypography = Typography(
    displayLarge = TextStyle(fontFamily=FontFamily.Default, fontWeight=FontWeight.W700, fontSize=36.sp, lineHeight=44.sp),
    displayMedium = TextStyle(fontFamily=FontFamily.Default, fontWeight=FontWeight.W700, fontSize=32.sp, lineHeight=40.sp),
    headlineLarge = TextStyle(fontFamily=FontFamily.Default, fontWeight=FontWeight.W600, fontSize=28.sp, lineHeight=36.sp),
    headlineMedium = TextStyle(fontFamily=FontFamily.Default, fontWeight=FontWeight.W600, fontSize=24.sp, lineHeight=32.sp),
    titleLarge = TextStyle(fontFamily=FontFamily.Default, fontWeight=FontWeight.W600, fontSize=18.sp, lineHeight=26.sp),
    titleMedium = TextStyle(fontFamily=FontFamily.Default, fontWeight=FontWeight.W500, fontSize=16.sp, lineHeight=24.sp),
    bodyLarge = TextStyle(fontFamily=FontFamily.Default, fontWeight=FontWeight.W400, fontSize=16.sp, lineHeight=24.sp),
    bodyMedium = TextStyle(fontFamily=FontFamily.Default, fontWeight=FontWeight.W400, fontSize=14.sp, lineHeight=20.sp),
    bodySmall = TextStyle(fontFamily=FontFamily.Default, fontWeight=FontWeight.W400, fontSize=12.sp, lineHeight=16.sp),
    labelLarge = TextStyle(fontFamily=FontFamily.Default, fontWeight=FontWeight.W600, fontSize=14.sp, lineHeight=20.sp),
    labelSmall = TextStyle(fontFamily=FontFamily.Default, fontWeight=FontWeight.W400, fontSize=10.sp, lineHeight=14.sp),
)