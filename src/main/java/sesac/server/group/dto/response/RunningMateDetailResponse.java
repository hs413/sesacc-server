package sesac.server.group.dto.response;

import sesac.server.group.entity.RunningMate;

public record RunningMateDetailResponse(
        String name,
        String subject,
        String goal,
        String course
) {

    private RunningMateDetailResponse(RunningMate runningMate) {
        this(
                runningMate.getName(),
                runningMate.getSubject(),
                runningMate.getGoal(),
                runningMate.getCourse().getName()
        );
    }

    public static RunningMateDetailResponse from(RunningMate runningMate) {
        return new RunningMateDetailResponse(runningMate);
    }
}
