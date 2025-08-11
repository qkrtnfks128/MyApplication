package com.example.myapplication.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

// CompositionLocal to provide a NavController across the app tree
val LocalAppNavController = staticCompositionLocalOf<NavHostController> {
  error("NavController not provided")
}


