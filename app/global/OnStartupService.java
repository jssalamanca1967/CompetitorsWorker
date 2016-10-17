package global;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.google.inject.Inject;
import play.Environment;
import play.libs.Akka;
import scala.concurrent.duration.Duration;
import worker.WorkerActor;

import java.util.concurrent.TimeUnit;

/**
 * Created by John on 07/10/2016.
 */
public class OnStartupService {

    @Inject
    private OnStartupService(Environment environment, ActorSystem system) {
        ActorRef actor = system.actorOf(WorkerActor.props);
        system.scheduler().schedule(
                Duration.create(0, TimeUnit.MILLISECONDS), //Initial delay 0 milliseconds
                Duration.create(2, TimeUnit.SECONDS),     //Frequency 30 minutes
                actor,
                "tick",
                system.dispatcher(),
                null
        );
    }

}
