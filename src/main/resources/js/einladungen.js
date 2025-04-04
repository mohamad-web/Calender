console.log("✅ Einladungsskript geladen!");

document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ Event Listener für Einladungen aktiviert!");

    // Einladung akzeptieren
    document.querySelectorAll(".accept-btn").forEach(button => {
        button.addEventListener("click", function () {
            let einladungId = this.getAttribute("data-id");
            akzeptiereEinladung(einladungId);
        });
    });

    // Einladung ablehnen
    document.querySelectorAll(".decline-btn").forEach(button => {
        button.addEventListener("click", function () {
            let einladungId = this.getAttribute("data-id");
            ablehnenEinladung(einladungId);
        });
    });
});

async function ablehnenEinladung(einladungId) {
    console.log(`🚫 Einladung ID ${einladungId} wird abgelehnt...`);

    try {
        let response = await fetch(`/api/einladungen/${einladungId}/ablehnen`, {
            method: "PUT"
        });

        if (!response.ok) {
            throw new Error("Fehler beim Ablehnen der Einladung.");
        }

        let message = await response.text();
        alert(message);
        location.reload();
    } catch (error) {
        console.error("❌ Fehler beim Ablehnen:", error);
        alert("Fehler beim Ablehnen: " + error.message);
    }
}

async function akzeptiereEinladung(einladungId) {
    console.log(`✅ Einladung ID ${einladungId} wird akzeptiert...`);

    try {
        let response = await fetch(`/api/einladungen/akzeptieren/${einladungId}`, {
            method: "PUT"
        });

        if (!response.ok) {
            throw new Error("Fehler beim Akzeptieren der Einladung.");
        }

        let message = await response.text();
        alert(message);
        location.reload();
    } catch (error) {
        console.error("❌ Fehler beim Akzeptieren:", error);
        alert("Fehler beim Akzeptieren: " + error.message);
    }
}

async function showEinladungen(terminId) {
    console.log("📨 Lade Einladungen für Termin ID:", terminId);

    try {
        let response = await fetch(`/api/termine/${terminId}/einladungen`);
        if (!response.ok) {
            throw new Error("Fehler beim Abrufen der Einladungen.");
        }
        let einladungen = await response.json();

        let einladungenListe = document.getElementById("einladungenListe");
        einladungenListe.innerHTML = "";

        einladungen.forEach(einladung => {
            let row = document.createElement("tr");
            row.innerHTML = `
                <td>${einladung.empfaenger}</td>  
                <td>${einladung.status}</td>
            `;
            einladungenListe.appendChild(row);
        });

        document.getElementById("einladungenModal").style.display = "block";

    } catch (error) {
        console.error("❌ Fehler:", error);
        alert("Fehler beim Laden der Einladungen.");
    }
}


function closeModal() {
    document.getElementById("einladungenModal").style.display = "none";

    document.addEventListener("DOMContentLoaded", function() {

        document.querySelectorAll(".accept-btn").forEach(button => {
            button.addEventListener("click", function () {
                let einladungId = this.getAttribute("data-id");

                fetch(`/api/einladungen/akzeptieren/${einladungId}`, {
                    method: "PUT"
                })
                    .then(response => {
                        if (!response.ok) throw new Error("Fehler beim Akzeptieren.");
                        return response.text();
                    })
                    .then(() => {
                        // Zeige ✅ anstelle des Buttons
                        let row = document.querySelector(`tr[data-id='${einladungId}']`);
                        let actionCell = row.querySelector("td:last-child");
                        actionCell.innerHTML = "<span style='color: green;'>✅</span>";
                    })
                    .catch(error => {
                        alert("Fehler: " + error.message);
                    });
            });
        });

        // Einladung ablehnen
        document.querySelectorAll(".decline-btn").forEach(button => {
            button.addEventListener("click", function () {
                let einladungId = this.getAttribute("data-id");

                fetch(`/api/einladungen/${einladungId}/ablehnen`, {
                    method: "PUT"
                })
                    .then(response => {
                        if (!response.ok) throw new Error("Fehler beim Ablehnen.");
                        return response.text();
                    })
                    .then(() => {
                        // Entferne die abgelehnte Einladung aus der Tabelle
                        let row = document.querySelector(`tr[data-id='${einladungId}']`);
                        row.remove();
                    })
                    .catch(error => {
                        alert("Fehler: " + error.message);
                    });
            });
        });

    });
}
