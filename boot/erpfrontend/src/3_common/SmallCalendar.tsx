import{useState} from "react";

const SmallCalendar = () => {
const [open,setOpen] = useState(false); 

const days = ["일","월","화","수","목","금","토"];
const dates = Array.from({length:30},(_, i) => i +1 );
    
    return(
        <>
<CalendarRow>
    
</CalendarRow>
        </>
    )
}
export default SmallCalendar