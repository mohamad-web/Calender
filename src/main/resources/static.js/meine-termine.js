document.addEventListener("DOMContentLoaded", function () {
    const erstellenBtn = document.getElementById("terminErstellenBtn");
    if (erstellenBtn) {
        erstellenBtn.addEventListener("click", erstelleTermin);
    }

    document.querySelectorAll(".delete-btn").forEach(button => {
        button.addEventListener("click", function(event) {
            event.preventDefault();
            const terminId = this.getAttribute("data-id");

            if (!confirm("Möchten Sie diesen Termin wirklich löschen?")) return;

            fetch(`/api/termine/loeschen/${terminId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (!response.ok) {
                        return response.text().then(text => { throw new Error(text); });
                    }
                    alert("Termin erfolgreich gelöscht!");
                    location.reload();
                })
                .catch(error => {
                    alert("Fehler beim Löschen: " + error.message);
                });
        });
    });
});

async function erstelleTermin() {
    let ausgewählteBenutzer = Array.from(document.querySelectorAll(".user-checkbox:checked"))
        .map(checkbox => checkbox.value);

    if (ausgewählteBenutzer.length === 0) {
        alert("Bitte wählen Sie mindestens einen Benutzer aus!");
        return;
    }

    let terminData = {
        title: document.getElementById("title").value,
        description: document.getElementById("description").value,
        startTime: document.getElementById("startTime").value,
        endTime: document.getElementById("endTime").value,
        eingeladeneBenutzer: ausgewählteBenutzer
    };

    try {
        let response = await fetch("/termine/neu", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(terminData)
        });

        if (!response.ok) {
            throw new Error("Fehler beim Erstellen des Termins.");
        }

        alert("Termin erfolgreich erstellt! Einladungen wurden gesendet.");
        window.location.href = "/dashboard";
    } catch (error) {
        console.error("🚨 Fehler:", error);
        alert("Ein Fehler ist aufgetreten.");
    }
}
