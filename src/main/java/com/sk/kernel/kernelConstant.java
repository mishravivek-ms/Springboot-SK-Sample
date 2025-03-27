package com.sk.kernel;

public class kernelConstant {
    public static String promptTemplate = """
            {{$input}}
            Extract the address,visting_hours,fee and contact from input. make sure the output in JSON in wll web format with address,visting_hours,fee and contact key value.
            """;

    public static String chapter1Template = """
            Answer the question in 10 line: {{$input}}
            """;
}
