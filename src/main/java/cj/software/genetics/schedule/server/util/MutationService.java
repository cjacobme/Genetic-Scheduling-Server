package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.Solution;
import cj.software.genetics.schedule.server.api.entity.SolutionPriority;
import cj.software.genetics.schedule.server.api.entity.Task;
import cj.software.genetics.schedule.server.entity.Coordinate;
import cj.software.genetics.schedule.server.exception.SlotOccupiedException;
import cj.software.util.spring.Trace;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Validated
public class MutationService {

    private final Logger logger = LogManager.getFormatterLogger();

    @Autowired
    private RandomService randomService;

    @Autowired
    private Converter converter;

    @Autowired
    private SolutionPriorityService solutionPriorityService;

    @Autowired
    private TaskService taskService;

    public void mutate(Solution source, @Trace double mutationRate) throws SlotOccupiedException {

        List<SolutionPriority> solutionPriorities = new ArrayList<>(solutionPriorityService.determinePriorities(source));
        int numPriorities = solutionPriorities.size();
        int selectedPriorityIndex = randomService.nextInt(numPriorities);
        int selectedPriority = solutionPriorities.get(selectedPriorityIndex).getValue();

        Map<Task, Coordinate> tasks = converter.toMapTaskCoordinate(source, selectedPriority);
        Map<Coordinate, Task> coordinatesMap = turnAround(tasks);
        List<Coordinate> coordinates = new ArrayList<>(coordinatesMap.keySet());
        coordinates.sort((o1, o2) -> {
            CompareToBuilder builder = new CompareToBuilder()
                    .append(o1.getWorkerIndex(), o2.getWorkerIndex())
                    .append(o1.getSlotIndex(), o2.getSlotIndex());
            int result = builder.build();
            return result;
        });
        for (Coordinate selectedCoordinate : coordinates) {
            double randomValue = randomService.nextDouble();
            if (randomValue < mutationRate) {
                int selectedWorkerIndex = selectedCoordinate.getWorkerIndex();
                int selectedSlotIndex = selectedCoordinate.getSlotIndex();
                Task selectedTask = coordinatesMap.get(selectedCoordinate);
                int otherIndex = randomService.nextInt(coordinates.size());
                Coordinate otherCoordinate = coordinates.get(otherIndex);
                int otherWorkerIndex = otherCoordinate.getWorkerIndex();
                int otherSlotIndex = otherCoordinate.getSlotIndex();
                Task otherTask = coordinatesMap.get(otherCoordinate);
                logger.info("swap coordinates between %s %s and %s %s",
                        selectedTask, selectedCoordinate, otherTask, otherCoordinate);
                taskService.deleteTaskAt(source, selectedPriority, selectedWorkerIndex, selectedSlotIndex);
                taskService.setTaskAt(source, selectedPriority, selectedWorkerIndex, selectedSlotIndex, otherTask);
                taskService.deleteTaskAt(source, selectedPriority, otherWorkerIndex, otherSlotIndex);
                taskService.setTaskAt(source, selectedPriority, otherWorkerIndex, otherSlotIndex, selectedTask);
            }
        }
    }

    private Map<Coordinate, Task> turnAround(Map<Task, Coordinate> source) {
        Map<Coordinate, Task> result = new HashMap<>();
        for (Map.Entry<Task, Coordinate> entry : source.entrySet()) {
            Task task = entry.getKey();
            Coordinate coordinate = entry.getValue();
            result.put(coordinate, task);
        }
        return result;
    }
}
