package com.waylau.easyexcel.export;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.ParameterizedType;
import java.util.List;
@Getter
@Setter
public class SheetParam<T> {
    private Class<T> clazz;
    String sheetName;

    List<T> sheetData;

    public SheetParam(Class<T> clazz, String sheetName, List<T> sheetData) {
        this.clazz = clazz;
        this.sheetName = sheetName;
        this.sheetData = sheetData;
    }

    /**
     * 获取泛型的类型
     * @return 泛型的类型
     */
    public Class<T> getGenericClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }
}
