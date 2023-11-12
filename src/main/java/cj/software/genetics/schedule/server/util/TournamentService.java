package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.Solution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TournamentService {

    @Autowired
    private RandomService randomService;

    @Autowired
    private SolutionService solutionService;

    public Solution select(List<Solution> solutions, int tournamentSize) {
        ArrayList<Solution> copy = new ArrayList<>(solutions);
        List<Solution> selected = new ArrayList<>(tournamentSize);
        for (int i = 0; i < tournamentSize; i++) {
            int numCopy = copy.size();
            int index = randomService.nextInt(numCopy);
            selected.add(copy.remove(index));
        }
        List<Solution> sorted = solutionService.sort(selected);
        Solution result = sorted.get(0);
        return result;
    }
}
