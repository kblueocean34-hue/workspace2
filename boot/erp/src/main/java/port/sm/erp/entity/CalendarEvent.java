package port.sm.erp.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="calendar_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarEvent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CALENDAR_SEQ")
	@SequenceGenerator(name = "CALENDAR_SEQ",sequenceName = "CALENDAR_EVENT_SEQ", allocationSize = 1)
	private Long id;

	
	@Column(nullable=false)
	private Long userId;
	
	/*날짜 필수*/
	@Column(nullable=false)
	private LocalDate eventDate;
	
	@Column(nullable = false, length = 100)
	  private String title;

	  @Column(length = 1000)
	  private String memo;

	  private LocalTime startTime;
	  private LocalTime endTime;
	
	
	
	
	
}
