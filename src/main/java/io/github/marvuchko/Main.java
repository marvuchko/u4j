package io.github.marvuchko;

import static io.github.marvuchko.U4J.ulid;

public class Main {
    public static void main(String[] args) {
        var ulid = ulid(); // ULID generated based on the current timestamp.
        System.out.println(ulid); // 01J867H5SF1P2H7S846Q2K1R1M
    }
}