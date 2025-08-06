package com.api.remittance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api.remittance.Entities.User;
import com.api.remittance.Entities.Wallet;
import com.api.remittance.Enum.Currency;
import com.api.remittance.Enum.UserType;
import com.api.remittance.Repositories.UserRepository;
import com.api.remittance.Repositories.WalletRepository;
import com.api.remittance.Services.PriceService;

@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(UserRepository repository, WalletRepository walletRepository, PriceService priceService) {

    return args -> {
      log.info("Preloading " + repository.save(new User("Joseph","josephalmeidazica@gmail.com",UserType.PF,"123456","12345678900")));
      log.info("Preloading " + walletRepository.save(new Wallet(repository.findByDocument("12345678900"), Currency.BRL, 1000.0)));
      log.info("Preloading " + walletRepository.save(new Wallet(repository.findByDocument("12345678900"), Currency.USD, 50.0)));
      log.info("Preloading " + repository.save(new User("Gabriel","gabriel.santos@gmail.com",UserType.PF,"123456","98765432100")));
      log.info("Preloading " + walletRepository.save(new Wallet(repository.findByDocument("98765432100"), Currency.BRL, 50.0)));
      log.info("Preloading " + walletRepository.save(new Wallet(repository.findByDocument("98765432100"), Currency.USD, 1000.0)));
      log.info(priceService.getPrice() != null ? "Preloading price service with value: " + priceService.getPrice() : "Failed to preload price service" );
    };
  }
}