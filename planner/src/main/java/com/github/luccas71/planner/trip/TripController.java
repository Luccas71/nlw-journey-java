package com.github.luccas71.planner.trip;

import com.github.luccas71.planner.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ParticipantService participantService;
    @PostMapping
    public ResponseEntity<CreateTripResponse> create(@RequestBody TripRequestPayload payload){
        Trip newTrip = new Trip(payload);

        this.tripRepository.save(newTrip);

        this.participantService.registerParticipantsToTrip(payload.emails_to_invite(), newTrip.getId());

        return ResponseEntity.ok(new CreateTripResponse(newTrip.getId()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTrip(@PathVariable UUID id){
        //a trip pode ou nao ser encontrada
        Optional<Trip> trip = this.tripRepository.findById(id);

        //condições
        // na primeira parte, se a trip for encontrada ela vai ser retornada junto com status ok, na segunda condição retorna um not found
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
