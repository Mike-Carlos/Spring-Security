// package com.activity.secure.Config;

// import javax.sql.DataSource;

// import org.springframework.boot.jdbc.DataSourceBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Profile;

// @Configuration
// public class DataSourceConfig {

//     @Bean
//     @Profile("dev")
//     public DataSource devDataSource() {
//         // Development datasource configuration
//         return DataSourceBuilder.create()
//             .url("jdbc:mysql://localhost:3306/mydb_dev")
//             .username("root")
//             .password("tgsivsbu")
//             .build();
//     }

//     @Bean
//     @Profile("prod")
//     public DataSource prodDataSource() {
//         // Production datasource configuration
//         return DataSourceBuilder.create()
//             .url("jdbc:mysql://prod-db:3306/proddb")
//             .username("${DB_USER}")
//             .password("${DB_PASS}")
//             .build();
//     }
// }
