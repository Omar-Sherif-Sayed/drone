package com.drone.error;

import com.drone.enums.DroneState;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "key")
public sealed class ApplicationFailed {

    private ApplicationFailed() {
    }

    @Getter
    public static final class Required extends ApplicationFailed {
        private final String fieldName;

        public Required(String fieldName) {
            this.fieldName = fieldName;
        }
    }

    @Getter
    public static final class MaxLength extends ApplicationFailed {
        private final String fieldName;
        private final Long length;

        public MaxLength(String fieldName, Long length) {
            this.fieldName = fieldName;
            this.length = length;
        }
    }

    @Getter
    public static final class MaxWeight extends ApplicationFailed {
        private final Long current;
        private final Long max;

        public MaxWeight(Long current, Long max) {
            this.current = current;
            this.max = max;
        }
    }

    @Getter
    public static final class MinCount extends ApplicationFailed {
        private final String fieldName;
        private final Integer length;

        public MinCount(String fieldName, Integer length) {
            this.fieldName = fieldName;
            this.length = length;
        }
    }

    @Getter
    public static final class PatternNotMatch extends ApplicationFailed {
        private final String fieldName;
        private final String description;

        public PatternNotMatch(String fieldName, String description) {
            this.fieldName = fieldName;
            this.description = description;
        }
    }

    @Getter
    public static final class AlreadyExist extends ApplicationFailed {
        private final String fieldName;

        public AlreadyExist(String fieldName) {
            this.fieldName = fieldName;
        }
    }

    @Getter
    public static final class NotExist extends ApplicationFailed {
        private final String fieldName;

        public NotExist(String fieldName) {
            this.fieldName = fieldName;
        }
    }

    @Getter
    public static final class NotSupported extends ApplicationFailed {
        private final String fieldName;

        public NotSupported(String fieldName) {
            this.fieldName = fieldName;
        }
    }

    @Getter
    public static final class DroneWrongState extends ApplicationFailed {
        private final List<DroneState> supportedStates;

        public DroneWrongState(List<DroneState> supportedStates) {
            this.supportedStates = supportedStates;
        }
    }

    @Getter
    public static final class BadDroneBattery extends ApplicationFailed {
        private final Long currentBatteryCapacity;
        private final Long minBatteryCapacity;

        public BadDroneBattery(Long currentBatteryCapacity, Long minBatteryCapacity) {
            this.currentBatteryCapacity = currentBatteryCapacity;
            this.minBatteryCapacity = minBatteryCapacity;
        }
    }

    public static final class AssignItemToChangeIdealDroneState extends ApplicationFailed {
    }

}
