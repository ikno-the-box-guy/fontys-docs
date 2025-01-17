describe('login', () => {
  it('passes', () => {
    cy.visit('http://localhost:5173/')

    cy.get('#email').type('erwtje013@gmail.com')

    cy.get('#password').type('123')

    cy.get('#login').click()

    cy.url().should('eq', 'http://localhost:5173/')

    cy.getCookies('token').should('exist')

    cy.window().then((window) => {
      const item = window.localStorage.getItem('user');
      expect(item).to.not.be.null;
    });

  })
  it('incorrect email', () => {
    cy.visit('http://localhost:5173/')

    cy.get('#email').type('fout@gmail.com')

    cy.get('#password').type('123')

    cy.get('#login').click()

    cy.url().should('eq', 'http://localhost:5173/login')

    cy.get('#login-error').should('contain', 'Incorrect credentials')
  })
  it('incorrect password', () => {
    cy.visit('http://localhost:5173/')

    cy.get('#email').type('erwtje013@gmail.com')

    cy.get('#password').type('1323')

    cy.get('#login').click()

    cy.url().should('eq', 'http://localhost:5173/login')

    cy.get('#login-error').should('contain', 'Incorrect credentials')
  })
})