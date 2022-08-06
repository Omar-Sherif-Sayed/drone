package com.drone.model;

import com.drone.enums.DroneModelName;
import com.drone.error.ApplicationFailed;
import io.vavr.control.Either;

import java.util.regex.Pattern;

public record CreateDroneRequest(String serial, DroneModelName model) {
    public Either<ApplicationFailed, Boolean> validate() {

        String serialFieldName = "serial";

        if (serial == null || serial.trim().equals(""))
            return Either.left(new ApplicationFailed.Required(serialFieldName));

        if (serial.length() > 100) return Either.left(new ApplicationFailed.MaxLength(serialFieldName, 100L));

        if (!Pattern.matches("^\\d+$", serial))
            return Either.left(new ApplicationFailed.PatternNotMatch(serialFieldName, "Allowed only Digits"));

        if (model == null) return Either.left(new ApplicationFailed.Required("model"));

        return Either.right(true);
    }

}
