package com.drone.response;

import com.drone.error.ApplicationFailed;

public record BaseResponse<T>(boolean stats, ApplicationFailed error, T response) {
}
