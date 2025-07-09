import { Navbar, Container, Nav } from "react-bootstrap";

const BottomNavbar = () => {
  return (
    <Navbar bg="dark" variant="dark" fixed="bottom" className="bottom-navbar">
      <Container>
        <Nav className="w-100 justify-content-around">
          <Nav.Link href="/home" className="text-center">
            <i className="bi bi-house-door-fill d-block fs-4"></i>
            <span className="nav-text">Home</span>
          </Nav.Link>
          <Nav.Link href="/collezione" className="text-center">
            <i className="bi bi-collection-fill d-block fs-4"></i>
            <span className="nav-text">Collezione</span>
          </Nav.Link>
          <Nav.Link href="/mazzi" className="text-center">
            <i className="bi bi-stack d-block fs-4"></i>
            <span className="nav-text">Mazzi</span>
          </Nav.Link>
          <Nav.Link href="/community" className="text-center">
            <i className="bi bi-people-fill d-block fs-4"></i>
            <span className="nav-text">Community</span>
          </Nav.Link>
        </Nav>
      </Container>
    </Navbar>
  );
};

export default BottomNavbar;
