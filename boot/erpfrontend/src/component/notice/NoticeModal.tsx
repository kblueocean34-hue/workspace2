import {Modal, Button, Form, ModalBody} from "react-bootstrap";

type NoticeRow = { //NoticeRow라는 타입을 정의합니다. 공지사항 한 개의 데이터 구조입니다.
id?:number, //id는 숫자형이고 선택값(?) 입니다.
title:string; //title은 문자열이며 필수값입니다.
content?:string;//content는 문자열이고 선택값입니다.
writer?:string;//writer는 작성자 이름, 선택값입니다.
createdAt?:string;//createdAt은 작성일자, 선택값입니다.
isPinned?:boolean;//상단 고정 여부, 선택값입니다.
viewCount?:number;
}

type Props = {
    show: boolean; //모달 표시 여부 (true면 열림).
    onHide:() => void; //모달 닫기 함수.
    data:NoticeRow | null; //현재 선택된 공지 데이터 (없으면 null).
    isEditMode: boolean; //수정 모드인지 여부.
    form:{
        title:string; content:string; isPinned:boolean;
    };
    setForm:React.Dispatch<React.SetStateAction<{//입력 폼 상태값 객체.
        title:string; content:string; isPinned:boolean;
    }>>;
    onSave:() => void; onDelete:() => void; onEditMode:() => void;
}

export default function NoticeModal({
show, onHide, data, isEditMode, form, setForm, onSave, onDelete, onEditMode,
}:Props){
    return(
<Modal show={show} onHide={onHide} size="lg">    
    <Modal.Header closeButton>
        <Modal.Title>
{isEditMode ? "공지사항 작성/수정" : "공지사항 상세"}
        </Modal.Title>
    </Modal.Header>

    <Modal.Body>
{isEditMode ?(
    <>

    <Form.Group className="mb-3">
        <Form.Label>제목</Form.Label>
        <Form.Control
        value={form.title}
        onChange={(e) =>
            setForm({...form, title:e.target.value})
        }
        />
    </Form.Group>

        <Form.Group className="mb-3">
        <Form.Label>내용</Form.Label>
        <Form.Control
        as="textarea"
        rows={6}
        value={form.content}
        onChange={(e) =>
            setForm({...form, content:e.target.value})
        }
        />
    </Form.Group>

<Form.Check
type="checkbox" label="상단고정" checked={form.isPinned}
onChange={(e) =>
    setForm({...form, isPinned:e.target.checked})
}
/>
</>
):(
<>
<h5>{data?.title}</h5>
<div className="">
    작성자:{data?.writer}
</div>
<div className="">
    {data?.content}
</div>
</>    
)}
</Modal.Body>

<Modal.Footer>
{!isEditMode &&(
<>
<Button variant="warning" onClick={onEditMode}>수정</Button>
<Button variant="danger" onClick={onDelete}>삭제</Button>
</>
)}
{isEditMode && (
<Button variant="primary" onClick={onSave}>저장</Button>
)}
<Button variant="secondary" onClick={onHide}>닫기</Button>
</Modal.Footer>
</Modal>
    );
}