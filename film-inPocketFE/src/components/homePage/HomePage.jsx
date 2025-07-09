import { Container, Row, Col, Image, Button } from "react-bootstrap";
import "../../styles/HomePage.css";
import TopNavbar from "./TopNavbar";
import BottomNavbar from "./BottomNavbar";
import TicketCounter from "./TicketCounter";

const HomePage = () => {
  const handleOpenPack = () => {
    alert("Pacchetto aperto! (Logica da implementare)");
  };

  return (
    <div className="home-page-container d-flex flex-column">
      <TopNavbar />

      <Container className="d-flex flex-column justify-content-center flex-grow-1 text-center">
        {/* Sezione superiore con Profilo e Contatore Ticket */}
        <Row className="align-items-center mb-auto pt-3 w-100">
          {/* Colonna per la foto profilo */}
          <Col xs={6} className="text-start">
            <a href="/profilo">
              <Image
                src="https://placehold.co/80x80/d4a24d/1a1a1a?text=FP"
                roundedCircle
                className="profile-pic"
              />
            </a>
          </Col>
          {/* Colonna per il contatore */}
          <Col xs={6} className="d-flex justify-content-end">
            <TicketCounter />
          </Col>
        </Row>

        {/* Pacchetto da Aprire e Bottone */}
        <div>
          <Row className="justify-content-center">
            <Col xs={8} md={6} lg={4}>
              <div className="card-pack-container">
                <div className="card-pack">
                  <span>?</span>
                </div>
              </div>
            </Col>
          </Row>
          <Row className="mt-4">
            <Col>
              <Button
                className="action-button open-pack-button"
                onClick={handleOpenPack}
              >
                Apri Pacchetto
              </Button>
            </Col>
          </Row>
        </div>

        {/* Questo div vuoto aiuta a spingere il contenuto verso il centro */}
        <div className="mb-auto"></div>
      </Container>

      <BottomNavbar />
    </div>
  );
};

export default HomePage;
