package auction.presentation.dto;

import auction.domain.AuctionSession;

import java.util.Date;

/**
 * Data Transfer Object for AuctionSession
 * Separates domain entities from presentation layer
 */
public class AuctionSessionDTO {
    private final String auctionId;
    private final String title;
    private String status;
    private Date startTime;
    private Date endTime;
    private int participantCount;
    private int lotCount;

    // Constructor from domain entity
    public AuctionSessionDTO(AuctionSession session) {
        this.auctionId = session.getAuctionId();
        this.title = session.getTitle();
        this.status = session.getStatus() != null ? session.getStatus().toString() : null;
        this.startTime = session.getStartTime();
        this.endTime = session.getEndTime();
        this.participantCount = session.getParticipants() != null ? session.getParticipants().size() : 0;
        this.lotCount = session.getCatalog() != null ? session.getCatalog().size() : 0;
    }

    // Constructor with fields
    public AuctionSessionDTO(String auctionId, String title, String status,
                             Date startTime, Date endTime, int participantCount, int lotCount) {
        this.auctionId = auctionId;
        this.title = title;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participantCount = participantCount;
        this.lotCount = lotCount;
    }

    // Getters
    public String getAuctionId() {
        return auctionId;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public int getLotCount() {
        return lotCount;
    }

    // Static factory method
    public static AuctionSessionDTO fromDomain(AuctionSession session) {
        return new AuctionSessionDTO(session);
    }

    @Override
    public String toString() {
        return "AuctionSessionDTO{" +
                "auctionId='" + auctionId + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", participantCount=" + participantCount +
                ", lotCount=" + lotCount +
                '}';
    }
}