package com.fathy.gatewayservice.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import net.minidev.json.JSONObject;

@JsonPropertyOrder({"RCSMessage", "reason"})
@ApiModel("Gsma Error")
public class GsmaErrorDTO {

    @JsonProperty("RCSMessage")
    @ApiModelProperty(name = "rcsMessage",  position = 1)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JSONObject rcsMessage;

    @JsonProperty("reason")
    @ApiModelProperty(name = "reason",  position = 2)
    private ReasonDTO reason;

    public GsmaErrorDTO() {
    }

    public GsmaErrorDTO(JSONObject rcsMessage, ReasonDTO reason) {
        this.rcsMessage = rcsMessage;
        this.reason = reason;
    }

    public JSONObject getRcsMessage() {
        return rcsMessage;
    }

    public void setRcsMessage(JSONObject rcsMessage) {
        this.rcsMessage = rcsMessage;
    }

    public ReasonDTO getReason() {
        return reason;
    }

    public void setReason(ReasonDTO reason) {
        this.reason = reason;
    }
}
