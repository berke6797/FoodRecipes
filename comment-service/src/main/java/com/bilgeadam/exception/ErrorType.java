package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    INTERNAL_ERROR(5100, "Sunucu Hatası", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4000, "Parametre Hatası", HttpStatus.BAD_REQUEST),
    RECIPE_NOT_FOUND(4400, "Böyle bir tarif bulunamadı", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND(4800, "Böyle bir yorum bulunamadı", HttpStatus.NOT_FOUND),
    COMMENT_EXISTS(4100,"Daha önce yorum yapmışsınız",HttpStatus.BAD_REQUEST),
    COULD_NOT_UPDATE_COMMENT(4900, "Başkasına ait yorumu güncelleyemezsiniz", HttpStatus.NOT_FOUND),
    COULD_NOT_DELETE_COMMENT(4200, "Başkasına ait yorumu silemezsiniz", HttpStatus.NOT_FOUND),
    ACTIVATE_CODE_ERROR(4500, "Aktivasyon kod hatası", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4600,"Geçersiz token bilgisi" ,  HttpStatus.BAD_REQUEST),
    POINT_EXISTS(4300,"Bu tarifi daha önce puanladınız.",HttpStatus.BAD_REQUEST),
    POINT_NOT_FOUND(4300,"Bu tarifi daha önce puanlamadınız !!",HttpStatus.BAD_REQUEST),
    COULD_NOT_DELETE_POINT(4700, "Başkasına ait puanı silemezsiniz", HttpStatus.NOT_FOUND);



    private int code;
    private String message;
    HttpStatus httpStatus;
}
