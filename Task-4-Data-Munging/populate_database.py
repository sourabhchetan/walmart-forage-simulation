import csv
import sqlite3
from collections import defaultdict

# =========================
# DATABASE CONNECTION
# =========================

connection = sqlite3.connect("shipment_database.db")
cursor = connection.cursor()

# =========================
# HELPER FUNCTION
# =========================

def get_product_id(product_name):
    """
    Returns product ID from product table.
    Creates product if it does not exist.
    """

    cursor.execute(
        "SELECT id FROM product WHERE name = ?",
        (product_name,)
    )

    result = cursor.fetchone()

    if result:
        return result[0]

    # Insert new product
    cursor.execute(
        "INSERT INTO product (name) VALUES (?)",
        (product_name,)
    )

    return cursor.lastrowid


# =========================
# PROCESS shipping_data_0.csv
# =========================

with open("shipping_data_0.csv", mode="r") as file:

    reader = csv.DictReader(file)

    for row in reader:

        product_name = row["product"]

        product_id = get_product_id(product_name)

        cursor.execute(
            """
            INSERT INTO shipment (
                product_id,
                quantity,
                origin,
                destination
            )
            VALUES (?, ?, ?, ?)
            """,
            (
                product_id,
                int(row["product_quantity"]),
                row["origin_warehouse"],
                row["destination_store"]
            )
        )


# =========================
# PROCESS shipping_data_1.csv
# =========================

# Structure:
# {
#   shipment_id: {
#       product_name: quantity
#   }
# }

shipment_products = defaultdict(lambda: defaultdict(int))

with open("shipping_data_1.csv", mode="r") as file:

    reader = csv.DictReader(file)

    for row in reader:

        shipment_id = row["shipment_identifier"]
        product_name = row["product"]

        shipment_products[shipment_id][product_name] += 1


# =========================
# PROCESS shipping_data_2.csv
# =========================

with open("shipping_data_2.csv", mode="r") as file:

    reader = csv.DictReader(file)

    for row in reader:

        shipment_id = row["shipment_identifier"]

        origin = row["origin_warehouse"]
        destination = row["destination_store"]

        if shipment_id not in shipment_products:
            continue

        # Insert aggregated shipment products
        for product_name, quantity in shipment_products[shipment_id].items():

            product_id = get_product_id(product_name)

            cursor.execute(
                """
                INSERT INTO shipment (
                    product_id,
                    quantity,
                    origin,
                    destination
                )
                VALUES (?, ?, ?, ?)
                """,
                (
                    product_id,
                    quantity,
                    origin,
                    destination
                )
            )


# =========================
# SAVE CHANGES
# =========================

connection.commit()
connection.close()

print("Database population completed successfully.")