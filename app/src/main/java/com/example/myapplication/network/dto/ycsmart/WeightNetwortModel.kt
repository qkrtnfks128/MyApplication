data class WeightNetworkModel(
    val device_name: String,
    val device_address: String,
    val device_type: String,
    val cvId: String,
    val tel: String,
    val connect: String,
    val regist: String,
    val datetime: String,
    val data: SevenElecScaleDevice
)

data class SevenElecScaleDevice(
    val body_data: ScaleBodyData
)

data class ScaleBodyData(
    val Date: String,
    val Time: String,
    val scale: Double,
    val bmi: Double,
    val fatRate: Double,
    val muscleRate: Double
)


data class WeightNetworkResponse(
    val status_code: Int,
    val judgment: WeightStatus
)



// fun ScaleBodyData.compare(scaleBodyData: ScaleBodyData): Boolean{
//     var retBoolean = true
//     if (this.Date != scaleBodyData.Date) retBoolean = false
//     if (this.Time != scaleBodyData.Time) retBoolean = false
//     if (this.scale != scaleBodyData.scale) retBoolean = false
//     return retBoolean
// }
