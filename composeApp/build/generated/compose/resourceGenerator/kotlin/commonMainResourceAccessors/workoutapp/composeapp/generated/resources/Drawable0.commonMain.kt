@file:OptIn(org.jetbrains.compose.resources.InternalResourceApi::class)

package workoutapp.composeapp.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi

private object CommonMainDrawable0 {
  public val compose_multiplatform: DrawableResource by 
      lazy { init_compose_multiplatform() }

  public val logo: DrawableResource by 
      lazy { init_logo() }
}

@InternalResourceApi
internal fun _collectCommonMainDrawable0Resources(map: MutableMap<String, DrawableResource>) {
  map.put("compose_multiplatform", CommonMainDrawable0.compose_multiplatform)
  map.put("logo", CommonMainDrawable0.logo)
}

internal val Res.drawable.compose_multiplatform: DrawableResource
  get() = CommonMainDrawable0.compose_multiplatform

private fun init_compose_multiplatform(): DrawableResource = org.jetbrains.compose.resources.DrawableResource(
  "drawable:compose_multiplatform",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(), "composeResources/workoutapp.composeapp.generated.resources/drawable/compose-multiplatform.xml", -1, -1),
    )
)

internal val Res.drawable.logo: DrawableResource
  get() = CommonMainDrawable0.logo

private fun init_logo(): DrawableResource = org.jetbrains.compose.resources.DrawableResource(
  "drawable:logo",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(), "composeResources/workoutapp.composeapp.generated.resources/drawable/logo.png", -1, -1),
    )
)
