data class BloodSugarNetworkModel(
    val device_name: String,
    val device_address: String,
    val device_type: String,
    val cvId: String,
    val tel: String,
    val connect: String,
    val regist: String,
    val datetime: String,
    val data: InBodyGLUDevice
)

data class InBodyGLUDevice(
    val body_data: GLUBodyData
)

data class GLUBodyData(
    val Date: String,
    val Time: String,
    val GlucoseResult: Int,
    val Temperature: Float
)


data class BloodSugarNetworkResponse(
    val status_code: Int,
    val judgment: BloodSugarStatus
)
