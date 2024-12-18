package tn.esprit.tahanaat.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.tahanaat.entity.Rencontre;
import tn.esprit.tahanaat.repo.RencontreRepository;

import java.time.LocalDate;
import java.util.List;

@Aspect
@Component
@Slf4j
@EnableScheduling
public class ArbitreAspect {

    @AfterThrowing(pointcut = "execution(* tn.esprit.tahanaat.service.RencontreServiceImpl.affecterArbitreARencontre(..))", throwing = "ex")
    public void logAffecterArbitreException(Exception ex) {
        log.warn("Il faut affecter un arbitre de meme niveau que la rencontre");
    }

    @Autowired
    private RencontreRepository rencontreRepository;

    @Scheduled(cron = "*/5 * * * * ?")
    public void rencontreDeDateSys() {
        List<Rencontre> rencontres = rencontreRepository.findByDateMatch(LocalDate.now());
        log.info("**** Les rencontres d'aujourd'hui****");
        for (Rencontre rencontre : rencontres) {
            log.info("Rencontre entre {} et {} à {}",
                    rencontre.getEquipeHome().getNomEquipe(),
                    rencontre.getEquipeAway().getNomEquipe(),
                    rencontre.getNomStade());
        }
    }
}