data class BloodPressureNetworkModel(
    val device_name: String,
    val device_address: String,
    val device_type: String,
    val cvId: String,
    val tel: String,
    val connect: String,
    val regist: String,
    val datetime: String,
    val data: InBodyBpDevice
)

data class InBodyBpDevice(
    val body_data: BPBodyData
)

data class BPBodyData(
    val Date: String,
    val Time: String,
    val SBP: Int, //최고혈압
    val DBP: Int, //최저혈압
    val HR: Int //맥박
)


data class BloodPressureNetworkResponse(
    val status_code: Int,
    val judgment: BloodPressureStatus
)



// fun BPBodyData.compare(bpBodyData: BPBodyData): Boolean{
//     var retBoolean = true
//     if (this.Time != bpBodyData.Time) retBoolean = false
//     if (this.SBP != bpBodyData.SBP) retBoolean = false
//     if (this.DBP != bpBodyData.DBP) retBoolean = false
//     if (this.HR != bpBodyData.HR) retBoolean = false
//     return retBoolean
// }
