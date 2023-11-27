package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.api.entity.SolutionPriority;
import cj.software.genetics.schedule.api.entity.Task;
import cj.software.genetics.schedule.api.exception.SlotOccupiedException;
import cj.software.genetics.schedule.server.entity.Coordinate;
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

    public void mutate(@Trace Solution source, @Trace double mutationRate) throws SlotOccupiedException {

        List<SolutionPriority> solutionPriorities = new ArrayList<>(solutionPriorityService.determinePriorities(source));
        SolutionPriority selectedPriority = randomService.nextFrom(solutionPriorities);
        int selectedPriorityValue = selectedPriority.getValue();

        Map<Task, Coordinate> tasks = converter.toMapTaskCoordinate(source, selectedPriorityValue);
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
                int otherIndex = randomService.nextInt(coordinates.size());
                Coordinate otherCoordinate = coordinates.get(otherIndex);
                while (otherCoordinate.equals(selectedCoordinate)) {
                    otherIndex = randomService.nextInt(coordinates.size());
                    otherCoordinate = coordinates.get(otherIndex);
                }
                Task otherTask = coordinatesMap.get(otherCoordinate);
                Task selectedTask = coordinatesMap.get(selectedCoordinate);
                logger.info("swap coordinates between %s %s and %s %s",
                        selectedTask, selectedCoordinate, otherTask, otherCoordinate);
                int otherWorkerIndex = otherCoordinate.getWorkerIndex();
                int otherSlotIndex = otherCoordinate.getSlotIndex();
                int selectedWorkerIndex = selectedCoordinate.getWorkerIndex();
                int selectedSlotIndex = selectedCoordinate.getSlotIndex();
                taskService.deleteTaskAt(source, selectedPriorityValue, selectedWorkerIndex, selectedSlotIndex);
                taskService.deleteTaskAt(source, selectedPriorityValue, otherWorkerIndex, otherSlotIndex);
                taskService.setTaskAt(source, selectedPriorityValue, selectedWorkerIndex, selectedSlotIndex, otherTask);
                taskService.setTaskAt(source, selectedPriorityValue, otherWorkerIndex, otherSlotIndex, selectedTask);
                coordinatesMap.put(otherCoordinate, selectedTask);
                coordinatesMap.put(selectedCoordinate, otherTask);
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
