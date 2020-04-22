package net;


import com.zuo.demo.lib_common.net.RespData;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CommonService {
	@POST("v1/live/index")
	Observable<RespData<NewsDetail>> login(@Body RequestBody body);
}
